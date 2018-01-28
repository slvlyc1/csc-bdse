#!/bin/sh

echo "Run scenarios for NodeApplication..."

curl -X POST --data-binary "One" "http://localhost:8080/upsert/A"
curl -X GET "http://localhost:8080/keys"
curl -X GET "http://localhost:8080/get/A"
curl -X GET "http://localhost:8080/get/B"

curl -X POST --data-binary "Two" "http://localhost:8080/upsert/A"
curl -X GET "http://localhost:8080/keys"
curl -X GET "http://localhost:8080/get/A"
curl -X GET "http://localhost:8080/get/B"

curl -X POST --data-binary "Three" "http://localhost:8080/upsert/B"
curl -X GET "http://localhost:8080/keys"
curl -X GET "http://localhost:8080/get/A"
curl -X GET "http://localhost:8080/get/B"
