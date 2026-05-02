void setupWiFi() {
  delay(10);
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(WIFI_SSID);

  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
  WIFI = 1;
}

void reconnectWiFi() {
  if (WiFi.status() != WL_CONNECTED && (millis() - lastReconnectAttempt > WIFI_RECONNECT_INTERVAL)) {
    Serial.println("WiFi terputus. Mencoba untuk menyambungkan kembali...");
    WiFi.disconnect();
    WiFi.begin(WIFI_SSID, WIFI_PASSWORD);

    // Tunggu koneksi sebentar
    unsigned long startAttemptTime = millis();
    while (WiFi.status() != WL_CONNECTED && millis() - startAttemptTime < WIFI_RECONNECT_INTERVAL / 2) {
      delay(500);
      Serial.print(".");
    }

    if (WiFi.status() == WL_CONNECTED) {
      Serial.println("\nWiFi berhasil tersambung kembali.");
      WIFI = 1;
    } else {
      Serial.println("\nGagal menyambungkan kembali WiFi.");
      WIFI = 0;
    }

    lastReconnectAttempt = millis();
  }
}

void Wireless_Telemetry() {
  if (enableWiFi == 1) {
    int LED1, LED2, LED3, LED4; 
    if (buckEnable == 1) { LED1 = 1; } else { LED1 = 0; }
    if (batteryPercent >= 99) { LED2 = 1; } else { LED2 = 0; }
    if (batteryPercent <= 10) { LED3 = 1; } else { LED3 = 0; }
    if (IUV == 0) { LED4 = HIGH; } else { LED4 = 0; }

    // Kirim data ke Firebase
    FirebaseJson json;
    json.set("powerInput", powerInput);
    json.set("batteryPercent", batteryPercent);
    json.set("voltageInput", voltageInput);
    json.set("currentInput", currentInput);
    json.set("voltageOutput", voltageOutput);
    json.set("currentOutput", currentOutput);
    json.set("temperature", temperature);
    json.set("Wh", Wh / 1000);
    json.set("energySavings", energySavings);
    json.set("LED1", LED1);
    json.set("LED2", LED2);
    json.set("LED3", LED3);
    json.set("LED4", LED4);
    json.set("voltageBatteryMin", voltageBatteryMin);
    json.set("voltageBatteryMax", voltageBatteryMax);
    json.set("currentCharging", currentCharging);
    json.set("electricalPrice", electricalPrice);

    // Kirim JSON ke Firebase
    if (Firebase.setJSON(fbdo, "/telemetry", json)) {
      // Serial.println("Data sent successfully!");
    } else {
      Serial.println("Failed to send data.");
      // Serial.println(fbdo.errorReason());
    }
  }

  if (enableBluetooth == 1) {
    // ADD BLUETOOTH CODE
  }
}
