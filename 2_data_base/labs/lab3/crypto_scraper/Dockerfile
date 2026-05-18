FROM golang:alpine AS builder
WORKDIR /app
COPY go.mod go.sum ./
RUN go mod download
COPY . .
RUN go build -o scraper cmd/scraper/main.go

FROM alpine:latest
WORKDIR /app
COPY --from=builder /app/scraper .
CMD ["./scraper"]