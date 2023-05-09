Running locally
1. mvn clean package
2. docker build --tag=user-ingredients-service:latest .
3. docker run -d -e AWS_ACCESS_KEY_ID=... -e AWS_SECRET_ACCESS_KEY=... -e AWS_SESSION_TOKEN=... -p8080:8080 user-ingredients-service:latest