SkyWatch is a Java-based console application that provides real-time weather information for any city in the world.
It demonstrates all four principles of Object-Oriented Programming (Abstraction, Encapsulation, Inheritance, Polymorphism) while using the free OpenWeatherMap API to fetch live weather data.

🚀 Features

Fetch real-time weather for any city worldwide

Shows temperature, humidity, wind speed, and weather condition

Uses all 4 OOP principles

Real API integration with error handling

Simple and clean console-based interface

📦 Project Structure

WeatherData.java
WeatherProvider.java
OpenWeatherMapProvider.java
SkyWatchApp.java

🧠 OOP Concepts Used
1. Encapsulation

WeatherData stores all details in private fields and exposes them using getters and setters.

2. Abstraction

WeatherProvider is an abstract class defining the method for getting weather data without specifying how.

3. Inheritance

OpenWeatherMapProvider extends WeatherProvider and implements actual API calling logic.

4. Polymorphism

The main class uses:
WeatherProvider provider = new OpenWeatherMapProvider();

This means you can replace it with another provider in the future.

🔗 API Used

OpenWeatherMap – Current Weather Data API
Endpoint: https://api.openweathermap.org/data/2.5/weather

You must include your own free API key inside the code.

▶️ How to Run

Compile all Java files
javac *.java

Run the main program
java SkyWatchApp

Enter a city name and optional country code to get live weather data.

📘 Example Output
---------------- SKYWATCH WEATHER REPORT ----------------
Location : Delhi, IN
Condition: Clear Sky
Temp : 27.5 °C
Feels like: 28.1 °C
Humidity : 45 %
Wind : 3.8 km/h
🛠 Technologies Used

Java

HTTPURLConnection

JSON parsing

OpenWeatherMap API

Core OOP concepts

🌟 Future Improvements

Add 7-day forecast

Add air quality index

Add GUI (JavaFX / Swing)

Save search history

