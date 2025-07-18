# 📰 Problem Statement

**In today's fast-paced world, people often struggle to keep up with the overwhelming volume and length of news articles. Newspapers and online news sources are filled with lengthy content, making it difficult for readers to find time to read and absorb all the important information. This leads to missed key facts, lack of awareness, and information overload.**

---

# 🛠️ Solution Approach

**To address this, we built a web application that allows users to:**

- Quickly save news articles or personal notes as journal entries.
- Instantly extract the main content from news article URLs, removing clutter and irrelevant sections.
- Automatically generate concise, AI-powered summaries of articles and notes, so users can grasp the essential points without reading the entire text.
- Manage and review all their summarized news and notes in a modern, user-friendly interface.

---

# News Journal Summariser

A modern Spring Boot web application for creating, managing, and summarizing news journal entries with AI-powered summarization and web scraping.

## ✨ Features

- **📝 Journal Entry Management:** Create, view, edit, and manage personal journal entries.
- **🔗 URL Content Extraction:** Paste a news article URL to auto-extract the main content.
- **🤖 AI Summarization:** Summarize articles or notes using Ollama (Llama2 model) running locally.
- **🎨 Modern UI:** Beautiful, responsive dark-themed interface.
- **📱 Mobile-Friendly:** Works on all devices.
- **⚡ Real-time Processing:** Instant content extraction and summarization.

## 🛠️ Technology Stack

- **Backend:** Spring Boot (Java 21)
- **Frontend:** Thymeleaf, CSS, JavaScript
- **AI Integration:** Ollama (Llama2)
- **Web Scraping:** JSoup
- **Build Tool:** Maven

## 📋 Prerequisites

- **Java 21+**
- **Maven 3.6+**
- **Ollama** (with Llama2 model downloaded and running locally)

## 🚀 Installation & Setup

1. **Install Ollama**

   - **macOS:** `brew install ollama`
   - **Linux:** `curl -fsSL https://ollama.ai/install.sh | sh`
   - **Windows:** Download from [https://ollama.ai/download](https://ollama.ai/download)

2. **Download Llama2 Model**

   ```bash
   ollama serve
   ollama pull llama2
   ```

3. **Clone and Run the Application**

   ```bash
   git clone <your-repo-url>
   cd News Journal Summariser
   mvn clean install
   mvn spring-boot:run
   ```

   The app will be available at `http://localhost:8080`.

## 🎯 How to Use

- **Add Entry:** Type your note or paste a news article URL (toggle between modes).
- **Summarize:** Click "Summarise" on any entry to generate an AI summary.
- **Edit:** Click "Modify" to update an entry.
- **View All:** See all entries in a table; summaries appear below the table.

## 🏗️ Project Structure

```
src/
├── main/
│   ├── java/com/journalapp/journalApp/
│   │   ├── controller/
│   │   │   ├── JournalEntryController.java
│   │   │   └── HealthCheck.java
│   │   ├── entity/
│   │   │   └── JournalEntry.java
│   │   └── JournalAppApplication.java
│   └── resources/
│       ├── static/
│       │   ├── journal.js
│       │   └── style.css
│       ├── templates/
│       │   ├── index.html
│       │   ├── all-entries.html
│       │   └── style.css
│       └── application.properties
```

## ⚙️ Configuration

Edit `src/main/resources/application.properties` as needed:

```properties
spring.application.name=Journal App
spring.ai.ollama.base-url=http://localhost:11434
spring.ai.ollama.model=llama2
```

## 🔍 How It Works

- **Web Scraping:** Uses JSoup to extract main article content from URLs, falling back to cleaned body text if needed.
- **AI Summarization:** Sends content to Ollama’s Llama2 model for summarization, handling streaming responses and formatting for web display.
- **Modern UI:** Responsive, dark-themed, with smooth animations and clear feedback.

## 🐛 Troubleshooting

- **Ollama Connection Error:** Ensure Ollama is running and the model is downloaded.
- **Web Scraping Issues:** Some sites may block scraping; try different sources.
- **Port Conflicts:** Change the port in `application.properties` if needed.

## 🤝 Contributing

1. Fork the repo
2. Create a feature branch
3. Commit and push your changes
4. Open a pull request


---

⭐ If you find this project helpful, please give it a star!

---
