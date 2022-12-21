# Dependency Injection

## Commando's

- Code compileren: `./gradlew build`
- Code testen: `./gradlew test`
- Demo-applicatie uitvoeren: `./gradlew run`

## Uitleg over framework

### TODO: (in de readme)
zeggen hoe het constructor kiezen werkt (zie documentation van DependencyContainer)
zeggen hoe je de package meegeeft (gwn een class in de root daarvan meegeven)
zeggen da het interfaces ondersteund ma da die wel annotated moeten zijn
zeggen da beans werken zolang je ze defineert in een @Configuration klasse,
en dat ze afhankelijk mogen zijn van andere beans of components.
zeggen da interception nie ondersteund is op eagerinit classes en mssn of throwen dan.


### TODO: (in de code)
- [x] component scanning
- [x] constructor injection
- [x] @Configuration
- [x] @Bean
- [x] @Component
- [x] @Controller
- [x] @Service
- [x] @Repository
- [x] Interception
- [x] zo min mogelijk reflection na launch
- [x] onderhoudbaar (strategy pattern bij interception)
- unit tests
  - [x] components worden aangemaakt
  - [x] beans worden aangemaakt
  - [x] cyclishe dependencies worden gedetecteerd
  - [x] interception tests
- [ ] readme (zie opdracht en boven)
- [x] keyboard input detection naar controllers
- [x] demo applicatie
- [x] logging verschillende niveau's (Het moet mogelijk zijn om, mits aanpassing van het logging niveau, te kunnen zien welke objecten wanneer aangemaakt worden.
)