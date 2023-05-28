#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <WiFiClient.h>

const char* ssid = "";
const char* password = "";
const int buttonPin = D6; // GPIO number for the button
const int greenLedPin = D1; // GPIO number for the green LED
const int redLedPin = D2; // GPIO number for the red LED
const String ipAddress = "104.248.80.209";
const String port = "8081";
const String endpoint = "/api/anonym/delivery/deliver-lunch";
const String supplierName = "Oliva";
const String companyAppToken = "e7a16db8-ab6a-421f-92ae-785828f7b044";

void setup() {
  Serial.begin(115200);
  pinMode(buttonPin, INPUT_PULLUP);
  pinMode(greenLedPin, OUTPUT);
  pinMode(redLedPin, OUTPUT);

  connectToWiFi();
}

void loop() {
  if (WiFi.status() != WL_CONNECTED) {
    connectToWiFi();
  }

  if (digitalRead(buttonPin) == LOW) {
    digitalWrite(greenLedPin, LOW);
    digitalWrite(redLedPin, LOW);

    if (WiFi.status() == WL_CONNECTED) {
      String url = "http://" + ipAddress + ":" + port + endpoint;
      String params = "?supplierName=" + supplierName + "&companyAppToken=" + companyAppToken;
      String fullUrl = url + params;

      WiFiClient client;
      HTTPClient http;
      Serial.println(fullUrl);
      http.begin(client, fullUrl);
      int httpResponseCode = http.POST("");
      if (httpResponseCode > 0) {
        String response = http.getString();
        Serial.println(httpResponseCode);
        Serial.println(response);

        blinkLed(greenLedPin);
      } else {
        Serial.print("Error on sending POST: ");
        Serial.println(httpResponseCode);

        blinkLed(redLedPin);
      }
      http.end();
    } else {
      Serial.println("Not connected to WiFi");
    }
    delay(200); // Debounce delay
  }
}

void connectToWiFi() {
  Serial.println("Connecting to WiFi...");
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(100);
    Serial.print(".");
  }
  Serial.println("Connected to WiFi");
}

void blinkLed(int ledPin) {
  digitalWrite(ledPin, HIGH);
  delay(1000);
  digitalWrite(ledPin, LOW);
}
