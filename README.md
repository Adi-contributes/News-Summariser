# 📰 News Journal Summariser

A modern Spring Boot web application that allows users to create journal entries and automatically summarize news articles using AI-powered text summarization. The application integrates with Ollama for local AI processing and includes web scraping capabilities to extract content from news URLs.

## ✨ Features

- **📝 Journal Entry Management**: Create, view, and manage personal journal entries
- **🔗 URL Content Extraction**: Automatically scrape and extract content from news article URLs
- **🤖 AI-Powered Summarization**: Generate comprehensive summaries using Ollama (Llama2 model)
- **🎨 Modern UI**: Beautiful, responsive dark-themed interface with smooth animations
- **📱 Mobile-Friendly**: Fully responsive design that works on all devices
- **⚡ Real-time Processing**: Instant content extraction and summarization
- **🔧 Easy Setup**: Simple configuration with local AI processing

## 🛠️ Technology Stack

- **Backend**: Spring Boot 3.5.3 (Java 21)
- **Frontend**: Thymeleaf templates with modern CSS and JavaScript
- **AI Integration**: Ollama with Llama2 model
- **Web Scraping**: JSoup library
- **Build Tool**: Maven
- **Styling**: Custom CSS with dark theme and animations

## 📋 Prerequisites

Before running this application, make sure you have:

- **Java 21** or higher
- **Maven** 3.6+
- **Ollama** installed and running locally
- **Llama2 model** downloaded in Ollama

## 🚀 Installation & Setup

### 1. Install Ollama

First, install Ollama on your system:

**macOS:**

```bash
brew install ollama
```

**Linux:**

```bash
curl -fsSL https://ollama.ai/install.sh | sh
```

**Windows:**
Download from [https://ollama.ai/download](https://ollama.ai/download)

### 2. Download Llama2 Model

Start Ollama and download the Llama2 model:

```bash
ollama serve
ollama pull llama2
```

### 3. Clone and Run the Application

```bash
# Clone the repository
git clone https://github.com/Adi-contributes/News-Summariser.git
cd News-Summariser

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## 🎯 How to Use

### Creating Journal Entries

1. **Manual Entry**: Simply type your content in the text area and click "Save Entry"
2. **URL Mode**:
   - Toggle "URL Mode"
   - Paste a news article URL
   - The app will automatically extract the article content
   - Click "Save Entry" to store it

### Summarizing Content

1. After creating an entry, click the "Summarise" button
2. The application will send the content to Ollama for AI-powered summarization
3. View the generated summary in the summary box below

### Managing Entries

- View all entries in the table format
- Edit entries by clicking the modify button
- Delete entries as needed
- Navigate between different views

## 🏗️ Project Structure

```
src/
├── main/
│   ├── java/com/journalapp/journalApp/
│   │   ├── controller/
│   │   │   └── JournalEntryController.java    # Main controller handling requests
│   │   ├── entity/
│   │   │   └── JournalEntry.java              # Data model for journal entries
│   │   └── JournalAppApplication.java         # Spring Boot main class
│   └── resources/
│       ├── static/
│       │   ├── journal.js                     # Frontend JavaScript
│       │   └── style.css                      # Additional styles
│       ├── templates/
│       │   ├── index.html                     # Main application page
│       │   ├── all-entries.html               # All entries view
│       │   └── style.css                      # Template styles
│       ├── application.properties             # Application configuration
│       └── architecture.puml                  # System architecture diagram
```

## ⚙️ Configuration

The application is configured via `src/main/resources/application.properties`:

```properties
spring.application.name=Journal App
spring.ai.ollama.base-url=http://localhost:11434
spring.ai.ollama.model=llama2
```

## 🔧 Key Features Explained

### Web Scraping

The application uses JSoup to intelligently extract article content from news URLs by:

- Trying common article selectors (article, .article-content, etc.)
- Falling back to cleaned body text if no article content is found
- Removing navigation, ads, and other non-content elements

### AI Summarization

- Integrates with Ollama's local AI processing
- Uses Llama2 model for high-quality summarization
- Handles streaming responses and formats output for web display
- Provides comprehensive, well-structured summaries

### Modern UI

- Dark theme with neon green accents
- Smooth animations and hover effects
- Responsive design for all screen sizes
- Intuitive user interface with clear visual feedback

## 🐛 Troubleshooting

### Common Issues

1. **Ollama Connection Error**

   - Ensure Ollama is running: `ollama serve`
   - Check if the model is downloaded: `ollama list`
   - Verify the base URL in application.properties

2. **Web Scraping Issues**

   - Some websites may block scraping
   - Try different news sources
   - Check the browser console for errors

3. **Port Already in Use**
   - Change the port in application.properties: `server.port=8081`

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature-name`
3. Commit your changes: `git commit -am 'Add feature'`
4. Push to the branch: `git push origin feature-name`
5. Submit a pull request

## 📄 License

This project is open source and available under the [MIT License](LICENSE).

## 👨‍💻 Author

**Adi-contributes** - [GitHub Profile](https://github.com/Adi-contributes)

---

⭐ If you find this project helpful, please give it a star!
