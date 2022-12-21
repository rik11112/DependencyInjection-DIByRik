# Dependency Injection

## Commando's

- Code compileren: `./gradlew build`
- Code testen: `./gradlew test`
- Demo-applicatie uitvoeren: `./gradlew run`

## Framework

Het framework is te vinden in de package `DIByRik`.

### Dependency Injection

- In de main methode van de applicatie wordt het framework geïnitialiseerd met de statische run methode van de `DIByRikApplication` klasse.
  - Deze neemt als parameter een `Class<?>`, dit moet een klasse zijn die rechtstreeks in de root package zit waarin je dependencies zitten.
  - Voorbeeld: `DIByRikApplication.run(Main.class);`
- Alle componenten moeten geannoteerd worden met `@Component` of één van zijn varianten. (zie [Component varianten](#component varianten))
- Je kan een component laten injecteren door het in de constructor te zetten.
  - Als er meerdere constructors zijn, kiest het framework de bovenste constructor in de klasse.
  - Je kan een constructor specifiëren door deze te annoteren met `@ConstructorInjection`.
    - Constructors met deze annotatie zullen altijd voorrang krijgen.
    - Merk op dat de annotatie niet verplicht is
- Interception wordt ook ondersteund (zie [Interception](#interception))

### Component varianten

Er zijn verschillende component varianten, sommigen zijn puur semantisch, anderen hebben praktische toepassingen.

- `@Component`: Dit is de basis variant.
- `@Service`: Dit is een puur semantische variant.
- `@Repository`: Dit is een puur semantische variant.
- `@Configuration`: Deze Annotatie duid aan dat de klasse Beans voorziet (zie [Beans](#beans)).
- `@Controller`: Deze Annotatie duid een Controller aan, binnen een controller kan je gebruik maken van InputMapping (zie [Input Mapping](#Input Mapping)).
- `@EagerInit`: Deze Annotatie zorgt ervoor dat de component onmiddellijk geïnitialiseerd wordt.
  - Normaal gezien worden componenten pas geïnitialiseerd wanneer ze voor het eerst nodig zijn.

### Beans

- Beans zijn objecten die door middel van methoden worden aangemaakt en doorgestuurd, je kan een bean maken door een methode te annoteren met `@Bean` in een Configuration klasse.
- Beans kunnen zelf ook dependencies hebben, dezen kan je laten injecteren door ze als parameter in de methode toe te voegen.
- Voor de rest werken ze exact hetzelfde als componenten, je kan ze injecteren door ze in de constructor van een component te zetten.

#### Voorbeeld:
```
@Configuration
public class Config {
    @Bean
    public KlasseDieGeBeantWord beanTest(String oneTwoThree, PrimeCalculator primeCalculator) {
        return new KlasseDieGeBeantWord(oneTwoThree + "-" + primeCalculator.amountOfPrimesUnder(100));
    }

    @Bean
    public String oneTwoThree() {
        return "123";
    }
}
```

Je kan nu `KlasseDieGeBeantWord`in een andere klasse de bean injecteren door de constructor te annoteren met `@Autowired` en de bean als parameter te zetten.``

### Input Mapping

- InputMapping is een manier om een methode aan te roepen door middel van standard input commando's.
- Standaard staat InputMapping aan, je kan het uitzetten door de `DIByRikApplication.run` methode aan te roepen met als 2de parameter `false`.
- Om input mapping te gebruiken annoteer je een methode met `@InputMapping(route = "route-naar-deze-endpoint")` in een controller.
  - de route mag geen spaties bevatten.
  - De methode moet een Object returnen, de toString methode van dit object zal als response naar de console worden gestuurd.
  - De methode mag input parameters hebben van volgende types:
    - `String`
    - `int` & `Integer`
    - `double` & `Double`
    - `boolean` & `Boolean`
    - `char` & `Character`
    - `long` & `Long`
    - `float` & `Float`
    - `short` & `Short`
    - `byte` & `Byte`

#### Voorbeeld:

In dit voor beeld is `ProgrammingLanguageRanking` een record.

```
@Controller
public class ProgrammingLanguagesController {
	private final ProgrammingLanguagesService programmingLanguagesService;

	public ProgrammingLanguagesController(ProgrammingLanguagesService programmingLanguagesService) {
		this.programmingLanguagesService = programmingLanguagesService;
	}

	@InputMapping(route = "getLanguageAtRank")
	public ProgrammingLanguageRanking getByPosition(int pos) {
		var language = programmingLanguagesService.getByPosition(pos);
		if (language == null) {
			throw new IllegalArgumentException("No language found at position " + pos);
		}
		return language;
	}

	@InputMapping(route = "getAllLanguages")
	public List<ProgrammingLanguageRanking> getAll() {
		return programmingLanguagesService.getAll();
	}
}
```

Input:
`getLanguageAtRank 5`
Output:
```
Dec 21, 2022 11:03:59 AM DIByRik.InputMapper start
INFO: ProgrammingLanguageRanking[position=5, name=JavaScript, percentage=9.286%]
```

Zoals je kan zien wordt de input gesplitst op de spaties, de eerste parameter is dus `"5"`, deze wordt omgezet van een String naar een int omdat de getByPosition methode een int als eerste parameter verwacht.

### Interception

Je kan een methode van een component laten intercepteren door de methode te annoteren met een Interception annotatie. (zie [Interception annotaties](#Interception annotaties))

**LET OP:**
- Interception werkt enkel wanneer een methode wordt opgeroepen door een andere component
  - een private methode zal dus nooit geïntercept worden, zelfs als je er een annotatie aan toevoegd.
  - als je een component zelf aanmaakt (dus niet injecteerd) zal interception ook niet werken.
    - (het is sowieso niet aan te raden om componenten zelf aan te maken, je zou ze altijd moeten injecteren)
- Je kan maximum 1 interception toevoegen aan een methode.

#### Interception annotaties

Er zijn een aantal standaard interceptions die je zo kan gebruiken.

- `@Cacheable`: Dit zal de output van een methode met een bepaalde input opslagen, wanneer de methode opnieuw wordt aangeroepen met dezelfde input zal de methode niet opnieuw uitgevoerd worden, maar zal de output van de vorige keer worden terug gegeven.
  - Dit is handig als je een methode hebt die een zware berekening uitvoert, maar je weet dat de output van de methode niet zal veranderen.
  - Let op: Side effects van deze methode zullen niet worden uitgevoerd, het is aangeraden deze annotatie alleen op pure methodes te gebruiken.
- `@Logged`: De entry en exit van de methode zal worden gelogd.
- `@Timed`: De tijd die de methode nodig heeft om uit te voeren zal worden gelogd.