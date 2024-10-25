
# Mazaady Task Android Application

This project is an Android application built to demonstrate form handling, category and subcategory selection, and localization. The app uses MVVM architecture with Jetpack components, Retrofit for networking, and Dagger Hilt for dependency injection.

## Project Overview

The Mazaady Task app allows users to select categories and subcategories from a predefined set and dynamically renders input fields based on the selected category. The app also supports multi-language functionality, with Arabic set as the default language.

## Features

- **Dynamic Form Generation**: Dynamically adds input fields based on category selection.
- **Category and Subcategory Selection**: Allows selection of categories with data retrieved from an API.
- **Dependency Injection**: Uses Dagger Hilt for clean dependency management.
- **Unit Testing**: Includes unit tests for repository classes.

## Architecture

This project follows the **MVVM (Model-View-ViewModel)** architecture, ensuring a clean separation of concerns and testability.

### Layers

1. **Model Layer** (`data/`): Handles data operations and defines the data model structure.
   - **Repositories**: Fetch data from the API or other data sources.
   - **Data Models**: Represent the data structure.
   - **Networking**: Uses Retrofit for network requests.

2. **ViewModel Layer** (`presentation/viewmodel/`): Manages UI-related data and communicates with the Model layer.
   - **ViewModels**: Expose data to the UI through LiveData.
   - **LiveData**: Observes changes in data and notifies the view layer.

3. **View Layer** (`presentation/view/`): Displays data to the user and observes the ViewModel.
   - **Fragments/Activities**: Render the UI components and observe data from ViewModels.
   - **Adapters**: Manage dynamic rendering in lists or containers.

4. **Dependency Injection** (`di/`): Uses Dagger Hilt to inject dependencies across the app.

5. **Utilities Layer** (`util/`): Provides helper functions and data state management.

### Benefits of MVVM in This Project

- **Scalability**: Easily add new features and data sources.
- **Testability**: Isolates business logic in ViewModels and Repositories for easy testing.
- **Separation of Concerns**: Each layer has a focused responsibility.

## Project Structure

- `data/`: Contains data models, repositories, and API service definitions.
- `presentation/`: Contains UI-related classes like Activities, Fragments, and ViewModels.
- `util/`: Utility classes and extension functions.
- `di/`: Dependency injection setup using Dagger Hilt.

## Setup Instructions

### Prerequisites

Ensure you have the following installed:

- [Android Studio](https://developer.android.com/studio)
- [JDK 8 or higher](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)

### Installation

1. Clone or download the project.
2. Open the project in Android Studio.
3. Sync Gradle by clicking on **File > Sync Project with Gradle Files**.
4. Add an API key or update any required API configurations in `local.properties` (if applicable).
5. Run the application on an emulator or a connected device.

## Dependencies

The project uses the following dependencies:

- **Retrofit**: For network requests
- **Gson**: For JSON parsing
- **Dagger Hilt**: For dependency injection
- **Jetpack Components**: Lifecycle, ViewModel, LiveData, and Navigation
- **Kotlin Coroutines**: For asynchronous programming
- **Mockito**: For mocking dependencies in unit tests
- **JUnit**: For writing and running unit tests

## Setting Arabic as Default Language

The app is configured to display Arabic as the default language. This is handled in `MainActivity` or `App` class by setting the locale to `ar`. See the `updateLocale` function in `App.kt` or `MainActivity.kt` for the implementation details.

## Running Tests

The app includes unit tests for the repository classes. To run the tests:

1. Open the **Project** view in Android Studio.
2. Navigate to `src/test/java` and locate the test classes.
3. Right-click on a test class or method and select **Run** to execute the test.

## Contributing

1. Fork the repository.
2. Create your feature branch (`git checkout -b feature/YourFeature`).
3. Commit your changes (`git commit -m 'Add some feature'`).
4. Push to the branch (`git push origin feature/YourFeature`).
5. Open a Pull Request.

## License

This project is licensed under the MIT License.
