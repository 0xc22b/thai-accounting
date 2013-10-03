# Resources
  - http://code.google.com/p/flying-saucer/
  - http://flyingsaucerproject.github.io/flyingsaucer/r8/guide/users-guide-R8.html
  - https://today.java.net/pub/a/today/2007/06/26/generating-pdfs-with-flying-saucer-and-itext.html
  - https://github.com/flyingsaucerproject/flyingsaucer

# jar files
* Latest version from mvn repository
* in class path (war/WEB-INF/lib/)
   - flying-saucer-core-9.0.3.jar (Need to be added in build path too.)
   - flying-saucer-pdf-9.0.3.jar (Need to be added in build path too.)
   - itext-2.1.7.jar (Need to be added in build path too.)
   - serializer-2.7.1.jar
   - xalan-2.7.1.jar
   - xml-apis-xerces-2.9.1.jar

# Embeded Font
  - Standard, available, only one that works for Thai: C:\\Windows\\Fonts\\arialuni.ttf

# CSS file relative path
  - setDocument with path that will be used as base path
    src: http://stackoverflow.com/questions/9722038/flying-saucer-itext-pdf-in-servlet-not-finding-css-file

# Caveats
  - Not supported in App Engine as java.awt.* not allowed to be use!
