# **☀️ Weather App – Technical Summary**

This document outlines the implementation details and architecture of the application

## Screenshots
<img width="350" alt="best_performers" src="https://github.com/user-attachments/assets/98ac2a12-4f46-413f-862f-1b342ddebeac" />
<img width="350" alt="worst_performers" src="https://github.com/user-attachments/assets/88639f62-2a35-4f2d-87a2-8e8657081aea" />


## **🧠 Implementation Decisions**

MVVM architecture was chosen because the app has a small number of interactions and UI states. MVVM provides clear separation of concerns and simplifies state management through unidirectional data flow.

## **🏗️ Architecture Overview**
The project is structured into 4 modules: **app**, **data**, and **domain**.

### **app** module
Contains all UI and presentation-layer logic.

#### **ViewModel**
- Observes data from Repositories
- Delegates data processing to mappers
- Handles user interactions
- Maintains view state and exposes UI-ready data classes

### **data** module
Responsible for providing data. Acts as the single source of truth.

### **domain** module
Defines interfaces and domain-level models.

## **🧪 Testing**
The project includes unit tests for key components:
- Data layer
- Mappers
- ViewModels

## **▶️ How to Use the App**
- **Add your OpenWeatherMap API key into gradle.properties file**  
  `OPEN_WEATHER_MAP_API_KEY=<your-api-key>`

- **Display weather forecast in the current city**  
  - Click the **target** icon in the top bar (top-right corner)
  - Give permission to access your location data

- **Display weather forecast for any other city**  
  - Click **search** icon in the top bar (top-right corner)
  - Input the city name into top bar
  - Click **search** icon in the top bar (top-right corner)
  - Select the exact city. There might be multiple cities with the same name.
