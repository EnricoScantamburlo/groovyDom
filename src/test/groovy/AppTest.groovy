/*
 * This Spock specification was generated by the Gradle 'init' task.
 */
import spock.lang.Specification
import groovy.xml.*
import static javax.xml.xpath.XPathConstants.NODESET
import org.w3c.dom.Document
import groovy.xml.dom.DOMCategory


class AppTest extends Specification {
    def "File exists"() {
        setup:

        when:
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("test.xml").getFile());

        then:
        file.isFile()
    }
    
    def "xpath should work"() {
        setup:
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("test.xml").getFile());

        def document
        file.withReader{ reader ->
            document = DOMBuilder.newInstance().parse(reader)          
        }
        
        when:
        def computerBooks
        def titles
        use (DOMCategory){ //Pimp my library pattern
            // I can search with the xpath syntax
            computerBooks = document.xpath("/catalog/book[genre='Computer']", NODESET)
        
            titles  = computerBooks.collect{ book ->
                
                book.title.text()
                    
            } as ArrayList
            
        }
        
        //println computerBooks.item(0)
        
        then:
        4 == computerBooks.length
        and:
            "XML Developer's Guide" == titles[0]
        and:
            "Microsoft .NET: The Programming Bible" == titles[1]
        and:
            "MSXML3: A Comprehensive Guide" == titles[2]
        and:
            "Visual Studio 7: A Comprehensive Guide" == titles[3]
        and:
        use (DOMCategory){
                "bk101" == computerBooks.item(0).'@id'
        }
    }
    
    def "normal parsing should work"() {
        setup:
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("test.xml").getFile());

        def document
        file.withReader{ reader ->
            document = DOMBuilder.newInstance().parse(reader)          
        }
        def catalog = document.documentElement
        
        when:
        def computerBooks 
        use (DOMCategory){ //Pimp my library pattern

            // normal iteration
            computerBooks = catalog.book.findAll{ book ->
                book.genre.text() == "Computer"                
            }        
        }
        
        //println computerBooks
        
        then:
        4 == computerBooks.size()
    }

}
