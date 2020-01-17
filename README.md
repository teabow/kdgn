# Kdgn

Kdgn is a Kotlin coDe GeNerator library.

Inspired by [sourcery](https://github.com/krzysztofzablocki/Sourcery).

Built on top of :

- [kastree](https://github.com/cretz/kastree) - Kotlin source AST & Syntax parsing
- [korte](https://korlibs.soywiz.com/korte/) - Template engine for Kotlin

Example of use :
--

Create a template file, for example `AutoPersistable.template` in a dedicated directory :

```
{% for type in types | implementing("AutoPersistable") %}
    import {{ type.packageName }}.{{ type.name }}
{% endfor %}

{% for type in types | implementing("AutoPersistable") %}
    fun {{ type.name }}.persist() {
        {% for member in type.members %}
        // {{ member.name }} = {{ member.annotations["persistence.defaultValue"] | raw }}
        {% endfor %}
    }
{% endfor %}
```

Then run the following kotlin script :

```kotlin
import com.cpzlabs.kdgn.compiler.compileTemplates

val templateSource = "path/to/template"
val packageSource = "path/to/src/package"
val packageDest = "path/to/src/generated"

compileTemplates(templateSource, packageSource, packageDest)
```

Custom commented annotations can be added into the code to be later interpreted in templates.

The following code :

```kotlin
class User: UserSpec, AutoPersistable {
    
    // kdgn:persistence: defaultValue = "A"
    val lastname: String? = null

    // kdgn:persistence: defaultValue = "B"
    val firstname: String? = null
    
}
```

will produce annotations available in templates :

```
{{ member.annotations["persistence.defaultValue"] | raw }}
```

Note: the `kdgn` prefix is required, then you can add an optional namespace (`persistence` in the above example).
