FROM golang:alpine AS builder
WORKDIR /app
COPY go.mod go.sum ./
RUN go mod download
COPY . .
RUN go build -o scraper cmd/scraper/main.go

FROM alpine:latest
RUN apk update && apk add --no-cache mpg123 alsa-utils alsa-lib
WORKDIR /app
COPY --from=builder /app/scraper .
COPY sound.mp3 .
CMD ["./scraper"]