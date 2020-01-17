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
