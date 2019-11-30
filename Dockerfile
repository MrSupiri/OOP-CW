FROM gradle:jdk11
WORKDIR /app
COPY . .
CMD [ "gradle", ":runAPI" ]