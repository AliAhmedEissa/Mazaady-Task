# NY Times Most Popular Articles App

A modern Android application built with **Clean Architecture**, **MVI Pattern**, and **Jetpack Compose** that displays the most popular articles from the NY Times API with custom animations and reusable components.

![Android](https://img.shields.io/badge/Platform-Android-green.svg)
![Kotlin](https://img.shields.io/badge/Language-Kotlin-blue.svg)
![Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-orange.svg)
![Architecture](https://img.shields.io/badge/Architecture-Clean%20Architecture-red.svg)
![Pattern](https://img.shields.io/badge/Pattern-MVI-purple.svg)

## 📱 Features

✅ **Clean Architecture** with modularization by feature  
✅ **MVI Pattern** for predictable state management  
✅ **Jetpack Compose** for modern UI development  
✅ **Custom Animations** including loading spinners and transitions  
✅ **Reusable Components** for maintainable code  
✅ **Pull-to-Refresh** functionality  
✅ **Real-time Search** with debouncing  
✅ **Period Filtering** (1 day, 7 days, 30 days)  
✅ **Error Handling** with retry functionality  
✅ **Image Loading** with Coil and placeholder animations  
✅ **Material 3 Design** with dynamic theming  
✅ **Dependency Injection** with Hilt  
✅ **Network Security** with certificate pinning  
✅ **Unit Testing** support across all layers  

## 🏗️ Architecture Overview

### Clean Architecture Layers

```
┌─────────────────────────────────────────────────┐
│                 Presentation                    │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐ │
│  │    UI       │ │ ViewModels  │ │   States    │ │
│  │ (Compose)   │ │   (MVI)     │ │ & Intents   │ │
│  └─────────────┘ └─────────────┘ └─────────────┘ │
└─────────────────────────────────────────────────┘
                        ↓
┌─────────────────────────────────────────────────┐
│                   Domain                        │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐ │
│  │  Entities   │ │ Use Cases   │ │ Repository  │ │
│  │ (Business)  │ │ (Business   │ │ Interfaces  │ │
│  │   Logic     │ │   Logic)    │ │             │ │
│  └─────────────┘ └─────────────┘ └─────────────┘ │
└─────────────────────────────────────────────────┘
                        ↓
┌─────────────────────────────────────────────────┐
│                    Data                         │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐ │
│  │ Repository  │ │   Mappers   │ │ Data Source │ │
│  │Implemention │ │   (DTO ↔    │ │  (Network)  │ │
│  │             │ │   Domain)   │ │             │ │
│  └─────────────┘ └─────────────┘ └─────────────┘ │
└─────────────────────────────────────────────────┘
```

### Module Structure

```
NYTimesApp/
├── app/                    # Main application module
├── core/                   # Shared utilities and base classes
└── features/articles/      # Articles feature with Clean Architecture
    ├── domain/            # Business logic (Pure Kotlin)
    ├── data/              # Data handling (Android)
    └── presentation/      # UI components (Compose)
```

## 🎯 Design Patterns

### 1. **Clean Architecture**
- **Separation of Concerns**: Each layer has distinct responsibilities
- **Dependency Inversion**: Dependencies point inward
- **Independent Testability**: Each layer can be tested in isolation

### 2. **MVI (Model-View-Intent)**
```kotlin
// State (Immutable)
data class ArticlesListState(
    val isLoading: Boolean = false,
    val articles: List<ArticleUiModel> = emptyList(),
    val error: String? = null
)

// Intent (User Actions)
sealed class ArticlesListIntent {
    object LoadArticles : ArticlesListIntent()
    data class SearchArticles(val query: String) : ArticlesListIntent()
}

// ViewModel (State Management)
class ArticlesListViewModel : BaseViewModel<State, Intent>()
```

### 3. **Repository Pattern**
```kotlin
// Interface in Domain Layer
interface ArticlesRepository {
    fun getMostPopularArticles(period: Period): Flow<Resource<List<Article>>>
}

// Implementation in Data Layer
class ArticlesRepositoryImpl : ArticlesRepository {
    override fun getMostPopularArticles(period: Period): Flow<Resource<List<Article>>> {
        // Implementation with caching and error handling
    }
}
```

### 4. **Use Case Pattern (Interactor)**
```kotlin
class GetMostPopularArticlesUseCase @Inject constructor(
    private val repository: ArticlesRepository
) : BaseUseCase<Params, List<Article>>() {
    
    data class Params(val filter: ArticleFilter)
    
    override fun execute(parameters: Params): Flow<Resource<List<Article>>> {
        // Business logic implementation
    }
}
```

### 5. **Mapper Pattern**
```kotlin
@Singleton
class ArticleMapper @Inject constructor() : BaseMapperImpl<ArticleDto, Article>() {
    override fun mapToDomain(dto: ArticleDto): Article {
        // DTO to Domain entity mapping
    }
}
```

### 6. **Dependency Injection (Hilt)**
```kotlin
@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    
    @Binds
    @Singleton
    abstract fun bindArticlesRepository(impl: ArticlesRepositoryImpl): ArticlesRepository
    
    @Provides
    @Singleton
    fun provideNYTimesApi(retrofit: Retrofit): NYTimesApi = retrofit.create(NYTimesApi::class.java)
}
```

## 🧱 Core Components

### Base Classes

#### **BaseViewModel** (MVI Implementation)
```kotlin
abstract class BaseViewModel<State, Intent> : ViewModel() {
    
    private val _state = MutableStateFlow(getInitialState())
    val state: StateFlow<State> = _state.asStateFlow()
    
    protected abstract fun getInitialState(): State
    abstract fun handleIntent(intent: Intent)
    
    protected fun setState(newState: State) {
        _state.value = newState
    }
    
    protected fun updateState(update: (State) -> State) {
        _state.value = update(_state.value)
    }
}
```

#### **BaseRepository** (Error Handling)
```kotlin
abstract class BaseRepositoryImpl : BaseRepository {
    
    protected suspend fun <T> safeApiCall(
        apiCall: suspend () -> Response<T>
    ): Flow<Resource<T>> = flow {
        emit(Resource.Loading)
        try {
            val response = apiCall()
            if (response.isSuccessful) {
                response.body()?.let { emit(Resource.Success(it)) }
                    ?: emit(Resource.Error(Exception("Response body is null")))
            } else {
                emit(Resource.Error(Exception("API Error: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e))
        }
    }
}
```

#### **BaseUseCase** (Business Logic)
```kotlin
abstract class BaseUseCase<in P, R>(
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    operator fun invoke(parameters: P): Flow<Resource<R>> = execute(parameters)
        .flowOn(coroutineDispatcher)

    protected abstract fun execute(parameters: P): Flow<Resource<R>>
}
```

## 🎨 Custom Animations

### Loading Animations

#### **Rotating Spinner**
```kotlin
@Composable
fun LoadingAnimation() {
    val rotation by rememberInfiniteTransition().animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing)
        )
    )
    
    Canvas(modifier = Modifier.size(60.dp)) {
        drawLoadingSpinner(rotation = rotation, color = MaterialTheme.colorScheme.primary)
    }
}
```

#### **Pulse Animation**
```kotlin
@Composable
fun PulseLoadingAnimation() {
    val scale by rememberInfiniteTransition().animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    Canvas(modifier = Modifier.size(40.dp)) {
        drawCircle(
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
            radius = size.minDimension / 2 * scale
        )
    }
}
```

### Screen Transitions
```kotlin
@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.articlesNavigation() {
    composable(
        route = ARTICLES_ROUTE,
        enterTransition = {
            slideInHorizontally(initialOffsetX = { -it }) + fadeIn()
        },
        exitTransition = {
            slideOutHorizontally(targetOffsetX = { -it }) + fadeOut()
        }
    ) {
        ArticlesListScreen()
    }
}
```

## 🧩 Reusable Components

### **ArticleCard** - Expandable with animations
```kotlin
@Composable
fun ArticleCard(
    article: ArticleUiModel,
    onClick: () -> Unit,
    isExpanded: Boolean = false
) {
    var imageLoading by remember { mutableStateOf(true) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Section Tag
            SectionTag(section = article.sectionTag)
            
            // Title with animation
            Text(
                text = article.title,
                style = MaterialTheme.typography.headlineSmall,
                maxLines = if (isExpanded) Int.MAX_VALUE else 2
            )
            
            // Image with loading animation
            if (article.hasMedia) {
                AsyncImage(
                    model = article.imageUrl,
                    contentDescription = article.title,
                    modifier = Modifier.fillMaxWidth(),
                    onSuccess = { imageLoading = false }
                )
                
                if (imageLoading) {
                    PulseLoadingAnimation()
                }
            }
            
            // Expandable content
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Text(text = article.abstract)
            }
        }
    }
}
```

### **LoadingComponent** - Centralized loading
```kotlin
@Composable
fun LoadingComponent(
    message: String = "Loading...",
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LoadingAnimation()
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}
```

### **ErrorComponent** - Consistent error handling
```kotlin
@Composable
fun ErrorComponent(
    message: String,
    onRetry: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Error,
            contentDescription = "Error",
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(64.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        
        onRetry?.let {
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = it) {
                Icon(Icons.Default.Refresh, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Retry")
            }
        }
    }
}
```

## 🔒 Security Implementation

### **Network Security**

#### Certificate Pinning
```kotlin
@Singleton
class CertificatePinner @Inject constructor() {
    fun getCertificatePinner(): okhttp3.CertificatePinner {
        return okhttp3.CertificatePinner.Builder()
            .add("api.nytimes.com", "sha256/CERTIFICATE_PIN_HERE")
            .build()
    }
}
```

#### API Key Protection
```kotlin
// Secure API key injection
@Provides
@Singleton
@Named("api_key")
fun provideApiKey(): String = BuildConfig.NY_TIMES_API_KEY

// ProGuard protection
-keep class com.boubyan.nytimesapp.BuildConfig { *; }
-obfuscate
```

#### Network Security Config
```xml
<network-security-config>
    <domain-config cleartextTrafficPermitted="false">
        <domain includeSubdomains="true">api.nytimes.com</domain>
        <pin-set>
            <pin digest="SHA-256">CERTIFICATE_PIN</pin>
        </pin-set>
    </domain-config>
</network-security-config>
```

### **Error Handling Strategy**

#### Global Error Handling
```kotlin
@Singleton
class ErrorHandler @Inject constructor(private val context: Context) {
    
    fun getErrorMessage(exception: Throwable): String {
        return when (exception) {
            is AppException.NetworkException -> context.getString(R.string.error_network)
            is AppException.UnauthorizedException -> context.getString(R.string.error_unauthorized)
            is AppException.ServerException -> context.getString(R.string.error_server)
            else -> context.getString(R.string.error_unknown)
        }
    }
}
```

#### Custom Exceptions
```kotlin
sealed class AppException(message: String, cause: Throwable? = null) : Exception(message, cause) {
    class NetworkException(message: String) : AppException(message)
    class UnauthorizedException(message: String) : AppException(message)
    class ServerException(message: String) : AppException(message)
    class ValidationException(message: String) : AppException(message)
}
```

## 🧪 Testing Strategy

### **Unit Tests Structure**

#### Domain Layer Testing
```kotlin
class GetMostPopularArticlesUseCaseTest {
    
    @Mock
    private lateinit var repository: ArticlesRepository
    
    private lateinit var useCase: GetMostPopularArticlesUseCase
    
    @Test
    fun `when repository returns success, then use case returns filtered articles`() = runTest {
        // Given
        val articles = listOf(createTestArticle())
        whenever(repository.getMostPopularArticles(any())).thenReturn(
            flowOf(Resource.Success(articles))
        )
        
        // When
        val result = useCase(GetMostPopularArticlesUseCase.Params()).first()
        
        // Then
        assertTrue(result is Resource.Success)
        assertEquals(articles, result.data)
    }
}
```

#### ViewModel Testing
```kotlin
class ArticlesListViewModelTest {
    
    @Mock
    private lateinit var getMostPopularArticlesUseCase: GetMostPopularArticlesUseCase
    
    @Test
    fun `when load articles intent, then state shows loading then success`() = runTest {
        // Given
        val articles = listOf(createTestArticleUiModel())
        whenever(getMostPopularArticlesUseCase(any())).thenReturn(
            flowOf(Resource.Loading, Resource.Success(articles))
        )
        
        // When
        viewModel.handleIntent(ArticlesListIntent.LoadArticles)
        
        // Then
        viewModel.state.test {
            assertEquals(true, awaitItem().isLoading)
            val successState = awaitItem()
            assertEquals(false, successState.isLoading)
            assertEquals(articles, successState.articles)
        }
    }
}
```

#### Repository Testing
```kotlin
class ArticlesRepositoryImplTest {
    
    @Mock
    private lateinit var remoteDataSource: ArticlesRemoteDataSource
    
    @Test
    fun `when api call succeeds, then return mapped articles`() = runTest {
        // Given
        val responseDto = createTestArticlesResponseDto()
        whenever(remoteDataSource.getMostPopularArticles(any())).thenReturn(
            Response.success(responseDto)
        )
        
        // When
        val result = repository.getMostPopularArticles(Period.SEVEN_DAYS).first()
        
        // Then
        assertTrue(result is Resource.Success)
        assertFalse(result.data.isEmpty())
    }
}
```

### **UI Testing**

#### Compose Testing
```kotlin
class ArticlesListScreenTest {
    
    @get:Rule
    val composeTestRule = createComposeRule()
    
    @Test
    fun whenLoadingState_thenShowLoadingIndicator() {
        composeTestRule.setContent {
            ArticlesListScreen(
                state = ArticlesListState(isLoading = true),
                onIntent = {}
            )
        }
        
        composeTestRule
            .onNodeWithText("Loading...")
            .assertIsDisplayed()
    }
}
```

## 🚀 Getting Started

### Prerequisites
- **Android Studio**: Hedgehog (2023.1.1) or later
- **JDK**: 8 or higher
- **Android SDK**: 24 or higher
- **NY Times API Key**: Get from [NY Times Developer Portal](https://developer.nytimes.com/get-started)

### Setup Instructions

#### 1. Clone Repository
```bash
git clone <repository-url>
cd NYTimesApp
```

#### 2. Configure API Key
1. Get your NY Times API key from https://developer.nytimes.com/get-started
2. Open `app/build.gradle.kts`
3. Replace the placeholder:
```kotlin
buildConfigField("String", "NY_TIMES_API_KEY", "\"YOUR_ACTUAL_API_KEY\"")
```

#### 3. Build and Run
```bash
# Clean build
./gradlew clean

# Build debug APK
./gradlew assembleDebug

# Run tests
./gradlew test

# Run on connected device
./gradlew installDebug
```

### Project Structure Details

#### **Core Module** (`core/`)
```
core/
├── base/                    # Base classes (Repository, UseCase, ViewModel)
├── network/                 # Network configuration and interceptors
├── error/                   # Error handling and mapping
├── security/                # Certificate pinning and security
├── utils/                   # Utilities and extensions
└── di/                      # Core dependency injection
```

#### **Articles Feature** (`features/articles/`)
```
articles/
├── domain/
│   ├── entities/           # Business entities (Article, Media, Period)
│   ├── repositories/       # Repository interfaces
│   ├── usecases/          # Business logic use cases
│   ├── exceptions/        # Domain-specific exceptions
│   ├── validators/        # Business rule validators
│   └── utils/             # Domain utilities
├── data/
│   ├── remote/
│   │   ├── api/           # Retrofit API interfaces
│   │   ├── dto/           # Data Transfer Objects
│   │   └── datasource/    # Remote data sources
│   ├── mappers/           # DTO ↔ Domain mapping
│   ├── repositories/      # Repository implementations
│   └── di/                # Data layer DI
└── presentation/
    ├── ui/
    │   ├── screens/       # Compose screens
    │   ├── components/    # Reusable UI components
    │   ├── states/        # MVI states
    │   └── intents/       # MVI intents
    ├── viewmodels/        # ViewModels with MVI
    ├── models/            # UI-specific models
    ├── mappers/           # Domain ↔ UI mapping
    └── navigation/        # Navigation configuration
```

#### **App Module** (`app/`)
```
app/
├── navigation/             # App-level navigation
├── di/                     # App-level dependency injection
├── ui/
│   ├── theme/             # Material Design theme
│   └── components/        # App-level UI components
└── MainActivity.kt        # Single activity
```

## 📊 Performance Optimizations

### **Memory Management**
- **Lazy Loading**: LazyColumn for efficient list rendering
- **Image Caching**: Coil with memory and disk caching
- **State Caching**: ViewModel state survival across configuration changes
- **Lifecycle Awareness**: Proper cleanup and resource management

### **Network Optimizations**
- **Request Caching**: 5-minute cache to reduce API calls
- **Debounced Search**: 300ms debouncing to minimize requests
- **Connection Pooling**: OkHttp connection reuse
- **Compressed Responses**: GZIP compression support

### **UI Performance**
- **Composition Optimization**: Stable composables and keys
- **Animation Performance**: Hardware-accelerated animations
- **Image Loading**: Progressive loading with placeholders
- **State Management**: Efficient state updates and recomposition

## 🔍 Troubleshooting

### Common Issues

#### **API Key Issues**
```
❌ 400 Bad Request - Invalid API Key
```
**Solution**: Ensure your API key is set correctly in `build.gradle.kts` and is valid.

#### **Network Issues**
```
❌ Network error: Unable to resolve host
```
**Solution**: Check internet connection and proxy settings.

#### **Build Issues**
```
❌ Duplicate class found
```
**Solution**: Clean project and check for duplicate dependencies.

### Debug Logging
Enable detailed logging by filtering Logcat for these tags:
- `NYTimes_API` - Network calls and responses
- `ArticlesRepository` - Repository operations
- `ArticlesListViewModel` - ViewModel state changes

## 📈 Future Enhancements

### Planned Features
- [ ] **Offline Support** - Cache articles for offline reading
- [ ] **Favorites** - Save articles for later reading
- [ ] **Dark Mode** - System-wide dark theme support
- [ ] **Push Notifications** - Breaking news notifications
- [ ] **Social Sharing** - Share articles to social media
- [ ] **Reading Progress** - Track reading progress
- [ ] **Accessibility** - Enhanced accessibility features

### Technical Improvements
- [ ] **Paging 3** - Infinite scrolling with paging
- [ ] **Room Database** - Local data persistence
- [ ] **Work Manager** - Background sync
- [ ] **Firebase Analytics** - User behavior tracking
- [ ] **Crashlytics** - Crash reporting
- [ ] **Performance Monitoring** - App performance metrics

## 🤝 Contributing

### Development Workflow
1. **Fork** the repository
2. **Create** a feature branch (`git checkout -b feature/amazing-feature`)
3. **Commit** your changes (`git commit -m 'Add amazing feature'`)
4. **Push** to the branch (`git push origin feature/amazing-feature`)
5. **Open** a Pull Request

### Code Standards
- **Kotlin Style Guide**: Follow official Kotlin coding conventions
- **Architecture**: Maintain Clean Architecture principles
- **Testing**: Add unit tests for new features
- **Documentation**: Update README and code comments
- **Performance**: Consider performance implications

### Pull Request Guidelines
- [ ] Code follows project architecture
- [ ] All tests pass
- [ ] Documentation updated
- [ ] No breaking changes
- [ ] Performance impact assessed

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📞 Support

For support and questions:
- **Issues**: [GitHub Issues](https://github.com/your-repo/issues)
- **Discussions**: [GitHub Discussions](https://github.com/your-repo/discussions)
- **Email**: your-email@example.com

---

**Built with ❤️ using Clean Architecture, MVI, and Jetpack Compose**
