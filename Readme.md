# Appium Android Mini-Framework

Framework mínimo para automatizar la app “My Demo App” (Android) ejecutando:
- En dispositivo/emulador local (Appium Server 2.x auto-gestionado desde el código).
- En Sauce Labs (dispositivo real bajo demanda).

Incluye:
- Gestión de variables de entorno via dotenv (.env).
- Arranque/parada de Appium Server local (AppiumServerManager).
- Resolución de capabilities (local/saucelabs) y creación del AndroidDriver (AndroidDriverManager).
- Page Object Model con pantallas y componentes reutilizables.
- Sincronización explícita por visibilidad, helpers y gestos básicos.

## Índice
- Requisitos
- Estructura del proyecto
- Configuración
  - Variables de entorno
  - Ejemplos de .env (Local y Sauce Labs)
- Ejecución de tests
- Detalles de implementación
  - Carga de entorno
  - Capabilities y selección de endpoint
  - Appium Server local
  - Page Object Model (pantallas y componentes)
  - Sincronización y helpers
- Extender el framework
- Problemas frecuentes (Troubleshooting)
- Notas y referencias

## Requisitos
- Java 17 y Maven 3.9+ instalados.
- Node.js LTS.
- Appium Server 2.x instalado (npm i -g appium) y drivers adecuados para Android (uiautomator2).
- Android SDK Platform Tools (adb) y un dispositivo real o emulador con depuración USB activa.
- Para Sauce Labs: cuenta con acceso a dispositivos reales, SAUCE_USERNAME y SAUCE_ACCESS_KEY, y la app subida a Sauce Storage (o ajusta la capability app).

## Estructura del proyecto (resumen)
- src/main/java
  - org.nttdata.config
    - Env: carga variables (dotenv + System env).
    - CapabilitiesResolver: construye UiAutomator2Options para Local y Sauce.
  - org.nttdata.drivers
    - AndroidDriverManager: crea/gestiona el ThreadLocal AndroidDriver y decide endpoint.
  - org.nttdata.server
    - AppiumServerManager: arranca/para Appium Server local por código.
  - org.nttdata.pom
    - MobileOperation: waits, acciones seguras y gestos.
    - MobileScreen: base de pantallas (health-check + waitUntilLoaded).
    - MobileComponent: base de componentes dentro de una pantalla.
- src/test/java
  - saucelabsdemoapp.tests
    - AndroidTestBase: @BeforeEach crea driver, @AfterEach hace quit (y para server local).
    - LocalSauceLabsDemoAppTest: ejemplos de prueba.
  - saucelabsdemoapp.screens / .components
    - Pantallas: ProductsScreen, AboutScreen (heredan BaseContentScreen).
    - Componentes: HeaderComponent, MenuComponent.

## Configuración

### Variables de entorno usadas
- RUN_TARGET: Local | SauceLabs
- APPIUM_IP, APPIUM_PORT: IP/puerto del Appium local (si no usas APPIUM_URL).
- APPIUM_URL: opcional; si se define, se usa directamente y NO se arranca Appium por código.
- NODE_PATH: ruta a node.exe (Windows) o bin/node (Unix).
- APPIUM_PATH: ruta a main.js de Appium 2.x (p.ej. …/node_modules/appium/build/lib/main.js).
- ANDROID_UDID: udid del dispositivo local (adb devices).
- APP_PACKAGE, APP_ACTIVITY: paquete/actividad de la app bajo prueba.
- SAUCE_USERNAME, SAUCE_ACCESS_KEY: credenciales Sauce Labs.
- SAUCELABS_TEST_URL: endpoint de ejecución (p.ej. https://ondemand.eu-central-1.saucelabs.com:443/wd/hub).

Nota: El loader (Env) busca primero variables de entorno del sistema y, si no, .env.

### Ejemplo .env para Local
```properties
# Local
RUN_TARGET=Local

# Appium Server
APPIUM_IP=127.0.0.1
APPIUM_PORT=4723
NODE_PATH=C:\Users\user\nodejs\node.exe
APPIUM_PATH=C:\Users\user\nodejs\node_modules\appium\build\lib\main.js

# Android
ANDROID_UDID=YOUR_DEVICE_UDID
APP_PACKAGE=com.saucelabs.mydemoapp.android
APP_ACTIVITY=com.saucelabs.mydemoapp.android.view.activities.SplashActivity

# Opcional: si defines APPIUM_URL, no se auto-arranca el server
# APPIUM_URL=http://127.0.0.1:4723
```

Cómo obtener UDID en Windows/Linux/Mac:
- Conecta el dispositivo/emulador y ejecuta: adb devices

### Ejemplo .env para Sauce Labs
```properties
# Sauce Labs
RUN_TARGET=SauceLabs

SAUCE_USERNAME=YOUR_USERNAME
SAUCE_ACCESS_KEY=YOUR_ACCESS_KEY
SAUCELABS_TEST_URL=https://ondemand.eu-central-1.saucelabs.com:443/wd/hub

# La app debe existir en Sauce Storage con este nombre o ajusta la capability en CapabilitiesResolver
# Por defecto: storage:filename=mda-2.2.0-25.apk
```

## Ejecución de tests
- Instala dependencias y compila:
  - mvn -q -DskipTests package
- Ejecuta todas las pruebas:
  - mvn -q test
- Ejecutar una clase concreta:
  - mvn -q -Dtest=LocalSauceLabsDemoAppTest test

## Detalles de implementación

### Carga de entorno (Env)
- Combina System.getenv con .env (dotenv-java). Las claves “required” lanzan error si faltan, facilitando fallos tempranos y más claros.

### Capabilities y selección de endpoint
- CapabilitiesResolver:
  - isLocal()/isSauceLabs() según RUN_TARGET.
  - androidLocalOptions: UiAutomator2 con udid, package y activity.
  - androidSauceOptions: UiAutomator2 con deviceName y platformVersion dinámicos y app en Sauce Storage.
- AndroidDriverManager:
  - Local:
    - Si APPIUM_URL no está definida, arranca Appium con AppiumServerManager y usa su URL.
    - Si APPIUM_URL está definida, se usa directamente y NO se arranca server.
  - Sauce Labs:
    - Usa SAUCELABS_TEST_URL y capabilities con sauce:options.

### Appium Server local (AppiumServerManager)
- Arranca con:
  - withAppiumJS(APPIUM_PATH) y usingDriverExecutable(NODE_PATH).
  - SESSION_OVERRIDE, timeout y log en appium.log.
- Manejo ThreadLocal para que cada hilo tenga su servicio (test paralelos).

### Page Object Model (POM)

Base:
- MobileOperation:
  - WebDriverWait, helpers seguros: waitVisible, clickWhenVisible, typeWhenVisible, textOf, isVisible.
  - Gestos básicos: swipeUp/Down/Left/Right.
  - Micro-assert: checkThat.
- MobileScreen:
  - uniqueElement() como health-check de pantalla.
  - isLoaded() y waitUntilLoaded() para sincronización robusta.
- MobileComponent:
  - Representa una sección dentro de una pantalla. Reutilizable y testeable aisladamente.

Implementación de la app demo:
- BaseContentScreen:
  - Header y menú lateral; menu() asegura abrir y esperar visibilidad.
- HeaderComponent:
  - tapHamburger() abre el menú (“View menu”).
- MenuComponent:
  - Acciones por item (About, Catalog, Log in, etc.).
- ProductsScreen, AboutScreen:
  - Cada una define su uniqueElement.

Patrón de uso:
```java
var products = new ProductsScreen(driver, Duration.ofSeconds(8)).waitUntilLoaded();
products.menu().tapAbout();
var about = new AboutScreen(driver, Duration.ofSeconds(8)).waitUntilLoaded();
```

### Tests de ejemplo
- LocalSauceLabsDemoAppTest
  - openApp: verifica que el package actual sea el esperado.
  - openAboutFromHamburgerMenu: navega desde Products al About a través del menú y valida carga.

### Sincronización y buenas prácticas
- Usa waitUntilLoaded() al crear pantallas para asegurar que su elemento único esté visible antes de interactuar.
- Emplea clickWhenVisible y waitVisible para minimizar flakiness.
- Preferir los waits del framework.

## Extender el framework

Añadir una nueva pantalla:
1) Crea una clase que extienda BaseContentScreen (o MobileScreen si no comparte header/menú).
2) Declara tus @AndroidFindBy y define uniqueElement().
3) Añade métodos de negocio (ej. tapX, fillY).
4) Usa new MiPantalla(driver, timeout).waitUntilLoaded() en tus tests.

Añadir un nuevo componente:
1) Crea una clase que extienda MobileComponent.
2) Pasa el container WebElement desde la pantalla.
3) Expone acciones/consultas propias del componente.

## Problemas frecuentes (Troubleshooting)
- Missing required env value: falta una variable obligatoria en .env o entorno del sistema. Revisa .envexample.
- Appium no arranca localmente:
  - Verifica NODE_PATH y APPIUM_PATH (rutas válidas y con permisos).
  - Puerto ocupado: cambia APPIUM_PORT o cierra instancias previas.
- No encuentra el dispositivo:
  - Revisa adb devices; habilita opciones de desarrollador y depuración USB.
  - Ajusta ANDROID_UDID.
- Sauce Labs falla al lanzar la app:
  - Asegúrate de que la app está subida: storage:filename=... (o proporciona una URL accesible en options.setApp()).
- Maven no ejecuta pruebas JUnit 5:
  - Añade junit-jupiter-engine y maven-surefire-plugin 3.x al pom.xml.

## Notas y referencias
- [Appium Java Client](https://github.com/appium/java-client)
- [UiAutomator2 Driver](https://github.com/appium/appium-uiautomator2-driver)
- [Sauce Labs](https://saucelabs.com/) Real Device Cloud
- [dotenv](https://github.com/cdimascio/dotenv-java) para gestión de variables