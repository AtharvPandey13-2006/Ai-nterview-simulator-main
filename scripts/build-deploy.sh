#!/bin/bash

# AI Interview Simulator - Build and Deployment Script
# This script handles the complete build and deployment process

set -e  # Exit on any error

echo "🚀 Starting AI Interview Simulator Build Process..."

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check prerequisites
check_prerequisites() {
    print_status "Checking prerequisites..."
    
    if ! command -v java &> /dev/null; then
        print_error "Java is not installed. Please install Java 17 or higher."
        exit 1
    fi
    
    if ! command -v mvn &> /dev/null && [ ! -f "./mvnw" ]; then
        print_error "Maven is not installed and mvnw wrapper not found."
        exit 1
    fi
    
    print_status "Prerequisites check passed ✅"
}

# Clean previous builds
clean_build() {
    print_status "Cleaning previous builds..."
    
    if [ -f "./mvnw" ]; then
        ./mvnw clean
    else
        mvn clean
    fi
    
    print_status "Clean completed ✅"
}

# Run tests
run_tests() {
    print_status "Running tests..."
    
    if [ -f "./mvnw" ]; then
        ./mvnw test
    else
        mvn test
    fi
    
    if [ $? -eq 0 ]; then
        print_status "All tests passed ✅"
    else
        print_error "Tests failed ❌"
        exit 1
    fi
}

# Build application
build_application() {
    print_status "Building application..."
    
    if [ -f "./mvnw" ]; then
        ./mvnw package -DskipTests
    else
        mvn package -DskipTests
    fi
    
    if [ $? -eq 0 ]; then
        print_status "Build completed successfully ✅"
    else
        print_error "Build failed ❌"
        exit 1
    fi
}

# Build Docker image
build_docker() {
    if command -v docker &> /dev/null; then
        print_status "Building Docker image..."
        
        docker build -t ai-interview-simulator:latest .
        
        if [ $? -eq 0 ]; then
            print_status "Docker image built successfully ✅"
        else
            print_error "Docker build failed ❌"
            exit 1
        fi
    else
        print_warning "Docker not found, skipping Docker build"
    fi
}

# Deploy to local environment
deploy_local() {
    print_status "Starting local deployment..."
    
    # Kill any existing process on port 8080
    if lsof -ti:8080 > /dev/null; then
        print_warning "Stopping existing process on port 8080..."
        kill -9 $(lsof -ti:8080) || true
    fi
    
    # Start the application
    if [ -f "./mvnw" ]; then
        nohup ./mvnw spring-boot:run > app.log 2>&1 &
    else
        nohup mvn spring-boot:run > app.log 2>&1 &
    fi
    
    echo $! > app.pid
    print_status "Application started in background (PID: $(cat app.pid))"
    print_status "Logs available in app.log"
    print_status "Application will be available at https://atharvpandey13-2006-github-io-interview-1-kq9g.onrender.com"
}

# Health check
health_check() {
    print_status "Performing health check..."
    
    sleep 10  # Wait for application to start
    
    for i in {1..30}; do
        if curl -s -o /dev/null -w "%{http_code}" https://atharvpandey13-2006-github-io-interview-1-kq9g.onrender.com/actuator/health | grep -q "200"; then
            print_status "Application is healthy ✅"
            return 0
        fi
        print_status "Waiting for application to start... ($i/30)"
        sleep 2
    done
    
    print_error "Health check failed ❌"
    return 1
}

# Deploy to production (Render)
deploy_production() {
    print_status "Preparing for production deployment..."
    
    # Check if git is clean
    if ! git diff-index --quiet HEAD --; then
        print_warning "You have uncommitted changes. Consider committing them first."
        read -p "Continue anyway? (y/N): " -n 1 -r
        echo
        if [[ ! $REPLY =~ ^[Yy]$ ]]; then
            exit 1
        fi
    fi
    
    # Push to main branch (triggers Render deployment)
    git push origin main
    
    print_status "Code pushed to main branch. Render will deploy automatically."
    print_status "Check deployment status at: https://dashboard.render.com"
}

# Main execution
main() {
    case "${1:-all}" in
        "check")
            check_prerequisites
            ;;
        "clean")
            clean_build
            ;;
        "test")
            run_tests
            ;;
        "build")
            build_application
            ;;
        "docker")
            build_docker
            ;;
        "local")
            check_prerequisites
            clean_build
            run_tests
            build_application
            deploy_local
            health_check
            ;;
        "prod")
            check_prerequisites
            clean_build
            run_tests
            build_application
            deploy_production
            ;;
        "all")
            check_prerequisites
            clean_build
            run_tests
            build_application
            build_docker
            ;;
        *)
            echo "Usage: $0 {check|clean|test|build|docker|local|prod|all}"
            echo ""
            echo "Commands:"
            echo "  check  - Check prerequisites"
            echo "  clean  - Clean previous builds"
            echo "  test   - Run tests"
            echo "  build  - Build application"
            echo "  docker - Build Docker image"
            echo "  local  - Deploy locally"
            echo "  prod   - Deploy to production"
            echo "  all    - Run all build steps (default)"
            exit 1
            ;;
    esac
}

# Trap to handle script interruption
trap 'print_error "Script interrupted"; exit 1' INT TERM

# Run main function
main "$@"

print_status "🎉 Build process completed successfully!"