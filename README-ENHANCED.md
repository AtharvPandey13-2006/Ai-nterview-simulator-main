# AI Interview Simulator - Enhanced Version

## 🚀 Features

### Core Functionality
- **AI-Powered Interviews**: Practice with realistic interview questions for various tech roles
- **Real-time Feedback**: Get instant, detailed feedback on your answers with scoring
- **Speech Recognition**: Answer questions using voice input (Chrome/Edge supported)
- **Multiple Roles**: Support for 10+ different tech roles including Software Engineer, Data Scientist, Product Manager, etc.
- **Progress Tracking**: Monitor your performance over time with detailed analytics

### Enhanced Features
- **Interactive Dashboard**: Comprehensive performance analytics with charts and insights
- **Session Management**: Secure user sessions with OAuth2 Google authentication
- **Mobile Responsive**: Optimized for all device sizes
- **Error Handling**: Robust error handling with user-friendly messages
- **Performance Optimization**: Caching, compression, and optimized database queries

## 🛠️ Technology Stack

### Frontend
- **React 18**: Component-based UI development
- **Tailwind CSS**: Utility-first CSS framework
- **Chart.js**: Interactive performance charts
- **Web Speech API**: Voice recognition capabilities

### Backend
- **Spring Boot 3**: Modern Java framework
- **Spring Security**: OAuth2 authentication and session management
- **MongoDB**: NoSQL database for storing user data and interview history
- **Gemini AI**: Advanced AI for generating questions and feedback

### Infrastructure
- **Docker**: Containerization support
- **Render**: Cloud deployment platform
- **Netlify**: Frontend hosting

## 📋 Prerequisites

Before running the application, ensure you have:

- Java 17 or higher
- Node.js 16+ (for development tools)
- MongoDB (local or cloud instance)
- Google OAuth2 credentials
- Gemini AI API key

## 🔧 Installation & Setup

### 1. Clone the Repository
```bash
git clone https://github.com/your-username/ai-interview-simulator.git
cd ai-interview-simulator
```

### 2. Environment Variables
Create a `.env` file in the root directory:

```env
# Database
MONGODB_URI=mongodb://localhost:27017/interview_simulator

# OAuth2
GOOGLE_CLIENT_ID=your-google-client-id
GOOGLE_CLIENT_SECRET=your-google-client-secret
REDIRECT_URI=https://atharvpandey13-2006-github-io-interview-1-kq9g.onrender.com/login/oauth2/code/google

# AI Service
GEMINI_API_KEY=your-gemini-api-key

# CORS
ALLOWED_ORIGINS=http://localhost:3000,http://127.0.0.1:5501

# Server
PORT=8080
```

### 3. Database Setup
```bash
# Start MongoDB (if running locally)
mongod --dbpath /your/mongodb/data/path

# Or use MongoDB Atlas (cloud)
# Update MONGODB_URI in your .env file
```

### 4. Backend Setup
```bash
# Install dependencies and run
./mvnw clean install
./mvnw spring-boot:run

# Or with custom profile
./mvnw spring-boot:run -Dspring-boot.run.profiles=enhanced
```

### 5. Frontend Development (Optional)
```bash
# For enhanced development experience
npm install -g live-server
live-server --port=3000 --cors
```

## 🐳 Docker Deployment

### Build and Run with Docker
```bash
# Build the image
docker build -t ai-interview-simulator .

# Run the container
docker run -p 8080:8080 \
  -e MONGODB_URI=your-mongodb-uri \
  -e GOOGLE_CLIENT_ID=your-client-id \
  -e GOOGLE_CLIENT_SECRET=your-client-secret \
  -e GEMINI_API_KEY=your-api-key \
  ai-interview-simulator
```

### Docker Compose
```yaml
version: '3.8'
services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - MONGODB_URI=mongodb://mongo:27017/interview_simulator
      - GOOGLE_CLIENT_ID=your-client-id
      - GOOGLE_CLIENT_SECRET=your-client-secret
      - GEMINI_API_KEY=your-api-key
    depends_on:
      - mongo
  
  mongo:
    image: mongo:5.0
    ports:
      - "27017:27017"
    volumes:
      - mongodb_data:/data/db

volumes:
  mongodb_data:
```

## 🔒 Security Features

- **OAuth2 Authentication**: Secure Google sign-in
- **CORS Protection**: Configured for specific origins
- **Session Security**: HTTP-only, secure cookies
- **Input Validation**: Comprehensive request validation
- **Error Handling**: Secure error responses without sensitive data exposure

## 📊 API Endpoints

### Authentication
- `GET /oauth2/authorization/google` - Initiate Google OAuth
- `GET /api/interview/me` - Get current user stats

### Interview Management
- `GET /api/interview/startInterview?role={role}` - Start new interview
- `POST /api/interview/submitAnswer` - Submit answer for evaluation
- `GET /api/interview/nextQuestion?role={role}&questionIndex={index}` - Get next question
- `GET /api/interview/score` - Get session score summary

## 🎯 Usage Guide

### Starting an Interview
1. Visit the application URL
2. Click "Start Practicing" to authenticate with Google
3. Select your target role from the dropdown
4. Click "Start Interview Practice"

### During the Interview
- **Type your answer** in the text area
- **Use voice input** by clicking the microphone button
- **Submit for feedback** to get AI evaluation
- **Get next question** to continue the interview
- **End interview** to return to role selection

### Viewing Analytics
- Click "Dashboard" to view your performance metrics
- See trends, strengths, weaknesses, and improvement areas
- Track your progress over time

## 🚀 Deployment

### Render Deployment
1. Connect your GitHub repository to Render
2. Set environment variables in Render dashboard
3. Deploy with automatic builds on push

### Netlify Frontend
1. Build the frontend assets
2. Deploy to Netlify with custom domain
3. Configure redirects for SPA routing

## 🧪 Testing

### Running Tests
```bash
# Unit tests
./mvnw test

# Integration tests
./mvnw test -Dtest=*IntegrationTest

# All tests with coverage
./mvnw clean test jacoco:report
```

### Manual Testing Checklist
- [ ] OAuth login/logout flow
- [ ] Interview start and question generation
- [ ] Answer submission and feedback
- [ ] Speech recognition functionality
- [ ] Mobile responsiveness
- [ ] Error handling scenarios

## 🔧 Troubleshooting

### Common Issues

**MongoDB Connection Failed**
- Verify MongoDB is running
- Check connection string format
- Ensure network connectivity

**OAuth Authentication Error**
- Verify Google OAuth credentials
- Check redirect URI configuration
- Ensure HTTPS in production

**Speech Recognition Not Working**
- Use Chrome or Edge browser
- Allow microphone permissions
- Check HTTPS requirement

**AI Feedback Issues**
- Verify Gemini API key
- Check API rate limits
- Monitor network connectivity

## 📈 Performance Optimization

### Implemented Optimizations
- Response compression (gzip)
- Database connection pooling
- Session caching
- Static asset optimization
- Lazy loading components

### Monitoring
- Application logs in `logs/interview-simulator.log`
- Performance metrics via Actuator endpoints
- Error tracking and alerting

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📞 Support

For support and questions:
- Email: support@interviewai.com
- GitHub Issues: [Create an issue](https://github.com/your-username/ai-interview-simulator/issues)
- Documentation: [Wiki](https://github.com/your-username/ai-interview-simulator/wiki)

## 🎉 Acknowledgments

- Google Gemini AI for intelligent interview feedback
- Spring Boot community for excellent framework
- React team for the amazing frontend library
- All contributors and beta testers

---

Made with ❤️ for helping developers ace their interviews!