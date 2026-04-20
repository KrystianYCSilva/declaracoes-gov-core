# Quickstart: Extensible Core

## Using a Custom Validator

```java
// 1. Define your custom validator
public class MyNisValidator extends NisValidator {
    @Override
    public boolean isValid(String value) {
        // custom logic here
        return super.isValid(value) && value.startsWith("1");
    }
}

// 2. Register it in the singleton
ValidatorRegistry.getInstance().register(Nis.class, new MyNisValidator());

// 3. Existing static calls now use your logic!
Nis.of("12345678901"); 
```

## Injecting a Custom JSON Mapper

```java
// Provide your own factory implementation
GovJsonFactory.setFactory(new MyCustomJsonFactory());

// Subsequent calls return your configured mapper
ObjectMapper mapper = GovJsonFactory.getMapper();
```
