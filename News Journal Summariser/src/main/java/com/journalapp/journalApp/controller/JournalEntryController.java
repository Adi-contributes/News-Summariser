package com.journalapp.journalApp.controller;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.journalapp.journalApp.entity.JournalEntry;

@Controller
@RequestMapping("/journal")
public class JournalEntryController {
    private final HashMap<Long, JournalEntry> journalEntries = new HashMap<>();
    private final HashMap<Long, String> scrapedContent = new HashMap<>(); // Store full scraped content
    private static long nextId = 1; // Static counter for auto-incrementing IDs

    @GetMapping({"/create", "", "/"})
    public String showCreateFormAndEntries(Model model) {
        model.addAttribute("journalEntry", new JournalEntry());
        model.addAttribute("entries", new ArrayList<>(journalEntries.values()));
        model.addAttribute("nextId", nextId); // Pass the next ID to the view
        return "index";
    }

    @PostMapping({"/create", "", "/"})
    public String createEntryMvc(@ModelAttribute JournalEntry journalEntry,
                                 @RequestParam(value = "isUrlMode", required = false, defaultValue = "false") boolean isUrlMode,
                                 @RequestParam(value = "url", required = false) String url,
                                 Model model) {
        // Auto-assign the next ID
        journalEntry.setId(nextId++);
        
        if (isUrlMode && url != null && !url.isBlank()) {
            try {
                Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                    .timeout(10000)
                    .get();
                
                // Try to extract main article content using common selectors
                String articleText = extractArticleContent(doc);
                
                // If no article content found, fall back to body text but clean it up
                if (articleText == null || articleText.trim().isEmpty()) {
                    articleText = cleanBodyText(doc.body().text());
                }
                
                System.out.println("Extracted content length: " + articleText.length());
                System.out.println("First 200 chars: " + articleText.substring(0, Math.min(200, articleText.length())));
                
                // Store the full scraped content separately for summarization
                scrapedContent.put(journalEntry.getId(), articleText);
                
                // Show user-friendly content in the display
                journalEntry.setContent("News Link: " + url + "\n\n[Article content extracted for summarization]");
            } catch (Exception e) {
                model.addAttribute("message", "Failed to fetch or parse URL: " + e.getMessage());
                model.addAttribute("journalEntry", new JournalEntry());
                model.addAttribute("entries", new ArrayList<>(journalEntries.values()));
                model.addAttribute("nextId", nextId);
                return "index";
            }
        }
        journalEntries.put(journalEntry.getId(), journalEntry);
        model.addAttribute("message", "Entry saved!");
        model.addAttribute("journalEntry", new JournalEntry());
        model.addAttribute("entries", new ArrayList<>(journalEntries.values()));
        model.addAttribute("nextId", nextId);
        return "index";
    }

    @GetMapping("/all")
    public String showAllEntries(Model model) {
        model.addAttribute("entries", new ArrayList<>(journalEntries.values()));
        model.addAttribute("nextId", nextId);
        return "all-entries";
    }
    public String callOllama(String prompt) throws Exception {
        String ollamaUrl = "http://localhost:11434/api/generate";
        java.util.Map<String, String> requestObj = new java.util.HashMap<>();
        requestObj.put("model", "llama2:latest");
        requestObj.put("prompt", prompt);
        String requestBody = new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(requestObj);
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(ollamaUrl))
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return prettyPrintOllamaResponse(response.body());
        }
    
    private String extractArticleContent(Document doc) {
        // Common selectors for article content
        String[] selectors = {
            "article",
            "[role='main']",
            ".article-content",
            ".post-content", 
            ".entry-content",
            ".story-content",
            ".content-body",
            ".article-body",
            ".post-body",
            ".entry-body",
            "main",
            ".main-content",
            "#content",
            ".content"
        };
        
        for (String selector : selectors) {
            try {
                org.jsoup.nodes.Element element = doc.select(selector).first();
                if (element != null) {
                    String text = element.text().trim();
                    if (text.length() > 200) { // Only use if substantial content found
                        return text;
                    }
                }
            } catch (Exception e) {
                continue;
            }
        }
        
        return null;
    }
    
    private String cleanBodyText(String bodyText) {
        if (bodyText == null || bodyText.trim().isEmpty()) {
            return "";
        }
        
        // Remove excessive whitespace and normalize
        String cleaned = bodyText.replaceAll("\\s+", " ").trim();
        
        // Remove common navigation and footer text patterns
        String[] patternsToRemove = {
            "cookie", "privacy", "terms", "subscribe", "newsletter", 
            "advertisement", "advert", "sponsored", "related articles",
            "share this", "follow us", "social media", "navigation",
            "menu", "footer", "header", "sidebar"
        };
        
        for (String pattern : patternsToRemove) {
            cleaned = cleaned.replaceAll("(?i)" + pattern + ".*?\\s", " ");
        }
        
        // Limit to reasonable length (first 3000 characters)
        if (cleaned.length() > 3000) {
            cleaned = cleaned.substring(0, 3000) + "...";
        }
        
        return cleaned;
    }
    
    private String prettyPrintOllamaResponse(String raw) {
        // Validate input
        if (raw == null || raw.trim().isEmpty()) {
            return "Error: Empty or null response from Ollama.";
        }
        
        try {
            // Ollama returns a streaming response with multiple JSON objects (one per line)
            // We need to parse each line and concatenate the response fields
            StringBuilder fullResponse = new StringBuilder();
            String[] lines = raw.split("\n");
            
            for (String line : lines) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                try {
                    com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                    com.fasterxml.jackson.databind.JsonNode jsonNode = mapper.readTree(line);
                    
                    // Extract the "response" field from each JSON object
                    if (jsonNode.has("response")) {
                        String response = jsonNode.get("response").asText();
                        if (response != null && !response.isEmpty()) {
                            fullResponse.append(response);
                        }
                    }
                } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
                    // Skip lines that aren't valid JSON
                    continue;
                }
            }
            
            String result = fullResponse.toString().trim();
            if (result.isEmpty()) {
                return "No response content found in Ollama response.";
            }
            
            // Convert literal \n strings to actual newlines and handle other formatting
            result = result.replace("\\n", "\n")
                          .replace("\\t", "\t")
                          .replace("\\r", "\r");
            
            // Convert newlines to HTML line breaks for proper display
            result = result.replace("\n", "<br>");
            
            // Debug: Print the final response
            System.out.println("Final response: [" + result + "]");
            
            return result;
            
        } catch (Exception e) {
            // Handle any other unexpected errors
            System.err.println("Unexpected error parsing Ollama response: " + e.getMessage());
            System.err.println("Raw response: " + raw);
            return "Error processing Ollama response: " + e.getMessage();
        }
    }
    
    
    
    @PostMapping("/summarise/{id}")
    public String summariseArticle(@PathVariable Long id, String modelName, Model model) {
        JournalEntry entry = journalEntries.get(id);
        if (entry == null) {
            model.addAttribute("summaryMessage", "Entry not found!");
        } else {
            String content = entry.getContent();
            String actualContent = content;
            
            // Check if we have scraped content for this entry
            String scrapedText = scrapedContent.get(id);
            if (scrapedText != null && !scrapedText.isEmpty()) {
                actualContent = scrapedText;
            }
            
            String promptText = "Please provide a comprehensive summary of this news article or content. Focus on the main points, key facts, and important details. Make it informative and well-structured:\n\n" + actualContent;
            String summary;
            try {
                summary = callOllama(promptText);
            } catch (Exception e) {
                summary = "Error during summarization: " + e.getMessage();
            }
            model.addAttribute("summaryMessage", summary);
        }
        model.addAttribute("journalEntry", new JournalEntry());
        model.addAttribute("entries", new ArrayList<>(journalEntries.values()));
        model.addAttribute("nextId", nextId);
        return "index";
    }

    @PostMapping("/update/{id}")
    public String updateEntry(@PathVariable Long id, 
                             @RequestParam("content") String newContent,
                             Model model) {
        JournalEntry entry = journalEntries.get(id);
        if (entry != null) {
            entry.setContent(newContent);
            model.addAttribute("message", "Entry updated successfully!");
        } else {
            model.addAttribute("message", "Entry not found!");
        }
        model.addAttribute("journalEntry", new JournalEntry());
        model.addAttribute("entries", new ArrayList<>(journalEntries.values()));
        model.addAttribute("nextId", nextId);
        return "index";
    }
}
