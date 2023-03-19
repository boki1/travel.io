<p align="center">
  <img src="https://user-images.githubusercontent.com/36764968/226183472-d363b3d1-b1e5-4d66-bdd1-a53168ea64a4.jpg" height="400" width="600" >
</p>

# travel.io - Let us find your next journey

Travel.io is an entertainment-focused hackathon project that aims to create personalized travel plans. Users can input their preferences in plain text, and the application will generate vacation offers based on their interests, budget, and travel dates.

## Features

- OpenAI API integration to process user inputs and generate recommendations for cities, countries, landmarks, and activities.
- Python-based text analysis to convert OpenAI API responses into structured JSON data.
- Spring Boot backend server with APIs to fetch hotel, landmark, and activity which are consistent with pricing information.
- React frontend application for user inputs and displaying vacation offers.

## How It Works

1. User enters plain text describing their travel preferences on the frontend side of the project.
2. The plain text is processed by the OpenAI API, which returns a response containing cities, countries, landmarks, and activities.
3. The response is analyzed by a Python analyzer and returned in JSON format.
4. The JSON data is sent to a Spring Boot backend server, which manages APIs providing information about hotels, their prices, landmarks, activities, and more.
5. Users can input their maximum budget, travel dates, and current location to receive personalized vacation offers that take these parameters into account.

## Quick Start

### 1. Clone the repository:
  ```text
  git clone https://github.com/yourusername/travel.io.git
  ```

### 2. Set up the flask API:
- Set up virtual environment
    ```
    python -m venv venv
    ```
- Activate the virtual environment:
  - On Windows:
    ```
    venv\Scripts\activate
    ```
  - On Linux:
    ```
    source venv/bin/activate
    ```
- Install dependencies:
    ```
    install_dependencies.sh
    ```
- Set up your openAI API key at `props.env` file
- Run the `run.py` file which will start the multithreaded flask server


### 3. Set up the backend server:

- Navigate to the `comm` directory.
- Update the `application.properties` file with your database and API credentials.
- Run the following command to start the Spring Boot server:

    ```
    ./mvnw spring-boot:run
    ```

### 4. Set up the frontend:

- Navigate to the `web` directory.
- Run `npm install` to install dependencies.
- Run `npm start` to start the React development server.

### 5. Start
- Open your web browser and navigate to `http://localhost:3000/` to view the application.

## Documentation

For detailed information about the project's components, APIs, and data structures, please refer to the [documentation](./docs) (still WIP).

## Contributing

We welcome contributions to improve and expand the capabilities of Travel.io. Please submit a pull request or open an issue if you have any ideas, bug reports, or suggestions.

## License

Travel.io is released under the [MIT License](./LICENSE).
