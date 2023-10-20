import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class XML {
    private static int Record_Num = 0;
    static File F_XML = new File("src\\Books.xml");
    static DocumentBuilderFactory XML_Factory = DocumentBuilderFactory.newInstance();
    static DocumentBuilder XML_Builder;
    static Document XML_Document = null;

    static Element XML_Document_Root = null;
    static Scanner input = new Scanner(System.in);
    //    static {
//        try {
//            builder = factory.newDocumentBuilder();
//        } catch (ParserConfigurationException e) {
//            e.printStackTrace();
//        }
//    }

    /////////////////////////////////////////////////////////////////////////////////////////////

    public XML() throws ParserConfigurationException {
        F_XML = new File("src\\Books.xml");
        XML_Factory = DocumentBuilderFactory.newInstance();
        XML_Builder = XML_Factory.newDocumentBuilder();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////

    public static ArrayList<Book> GetBooksData(int n) throws ParseException, IOException, SAXException {
        ArrayList<Book> Result = new ArrayList<>();
        /****************** CHECK ID IN *****************/
        ArrayList<String> All_Books_ID = new ArrayList<>();
        if (F_XML.exists())
        {
            Document GetFile = XML_Builder.parse(new File("src\\Books.xml"));
            NodeList ChildrenList = GetFile.getDocumentElement().getChildNodes();
            /************************************************/

            for (int i = 0; i < ChildrenList.getLength(); ++i) {
                Node Child = ChildrenList.item(i);

                if (Child.getNodeType() == Node.ELEMENT_NODE) {
                    Element found = (Element) Child;

                    String ID = ((Element) Child).getAttributes().getNamedItem("ID").getNodeValue();
                    System.out.println(ID);
                    All_Books_ID.add(ID.toLowerCase());
                }
            }
        }

        String ID = "";
        for (int i = 0; i < n; ++i) {
            System.out.println("====================================================================");
            System.out.println("Enter Data for Book " + (i + 1));
            System.out.print("Enter Book ID: ");
            while (true) {
                ID = input.next();
                if (!All_Books_ID.contains(ID.toLowerCase())) {
                    All_Books_ID.add(ID);
                    break;
                } else
                    System.out.println("Enter ID Name Again");
            }
            System.out.print("Enter Book Author: ");
            String q = input.nextLine();
            String Author = "";
            String Letters = "abcdefghijklmnopqrstuvwxyz";
            while (true) {
                Author = input.nextLine();
                Author = Author.trim();
                String AuthorLower = Author.toLowerCase();
                boolean All_IN_Letters = true;
                for (int j = 0; j < AuthorLower.length(); ++j) {
                    String letter = String.valueOf(AuthorLower.charAt(j));
                    if (!Letters.contains(letter)) {
                        All_IN_Letters = false;
                        break;
                    }
                }
                if (Author != "" && !Author.equalsIgnoreCase("null") && All_IN_Letters) {
                    break;
                } else
                    System.out.println("Enter Author Name Again");
            }
            System.out.print("Enter Book Title: ");
            String Title = "";
            while (true) {
                Title = input.nextLine();
                Title = Title.trim();
                System.out.println(Title);
                if (Title != "" && !Title.equalsIgnoreCase("null")) {
                    break;
                } else
                    System.out.println("Enter Title Name Again");
            }
            ArrayList<String> List = new ArrayList<>();
            List.add("drama");
            List.add("science");
            List.add("fiction");
            System.out.println(List);
            System.out.print("Enter Book Genre: ");
            String Genre = "";
            while (true) {
                Genre = input.nextLine();
                Genre = Genre.trim();
                if (Genre != "" && !Genre.equalsIgnoreCase("null") && List.contains(Genre.toLowerCase(Locale.ROOT))) {
                    break;
                } else
                    System.out.println("Enter Genre Name Again");
            }
            double Price = 0;
            Boolean Repeat = true;
            while (Repeat) {
                try {
                    System.out.print("Enter Book Price: ");
                    Price = input.nextDouble();
                    Repeat = false;
                } catch (Exception excpt) {
                    System.out.println("Wrong Input, Enter Price Again");
                    input.next();
                }
            }
            String oldstring;
            Date date = new Date();
            Repeat = true;
            q = input.nextLine();
            while (Repeat) {
                try {

                    System.out.print("Enter Book Publish Date 'Year-Month-Day': ");
                    oldstring = input.nextLine();
                    date = new SimpleDateFormat("yyyy-MM-dd").parse(oldstring);
                    Repeat = false;
                } catch (Exception e) {
                    System.out.println("wrong date format");
                }
            }
            System.out.print("Enter Book Description: ");
            String Description = input.nextLine();
            Book Book_added = new Book(ID, Author, Title, Genre, Price, date, Description);
            Result.add(Book_added);

        }
        return Result;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////

    public static void Addtag(Book b) {
        Element Book_Element = XML_Document.createElement("Book");
        Book_Element.setAttribute("ID", b.getID());
        Element Book_Property = null;

        Book_Property = XML_Document.createElement("Author");
        Book_Property.appendChild(XML_Document.createTextNode(b.getAuthor()));
        Book_Element.appendChild(Book_Property);

        Book_Property = XML_Document.createElement("Title");
        Book_Property.appendChild(XML_Document.createTextNode(b.getTitle()));
        Book_Element.appendChild(Book_Property);

        Book_Property = XML_Document.createElement("Genre");
        Book_Property.appendChild(XML_Document.createTextNode(b.getGenre()));
        Book_Element.appendChild(Book_Property);

        Book_Property = XML_Document.createElement("Price");
        double cur = b.getPrice();
        Book_Property.appendChild(XML_Document.createTextNode(Double.toString(cur)));
        Book_Element.appendChild(Book_Property);

        Book_Property = XML_Document.createElement("Publish_Date");
        String DateFormate = new SimpleDateFormat("yyyy-MM-dd").format(b.getPublish_Date());
        Book_Property.appendChild(XML_Document.createTextNode(DateFormate));
        Book_Element.appendChild(Book_Property);

        Book_Property = XML_Document.createElement("Description");
        Book_Property.appendChild(XML_Document.createTextNode(b.getDescription()));
        Book_Element.appendChild(Book_Property);

        XML_Document_Root.appendChild(Book_Element);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////

    public static void Addrecords() throws IOException, SAXException, TransformerException, ParseException {
        if (F_XML.exists()) {
            XML_Document = XML_Builder.parse(new File("src\\Books.xml"));
            XML_Document_Root = (Element) XML_Document.getDocumentElement();
        } else {
            XML_Document = XML_Builder.newDocument();
            XML_Document_Root = XML_Document.createElement("Catalogue");
        }
        System.out.println("Enter number of books");
        int Number_books = input.nextInt();
        ArrayList<Book> books = GetBooksData(Number_books);
        for (int i = 0; i < books.size(); i++) {
            Addtag(books.get(i));
        }
        if (!F_XML.exists()) {
            XML_Document.appendChild(XML_Document_Root);
        }

        DOMSource source = new DOMSource(XML_Document);
        Result result = new StreamResult(F_XML);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(source, result);
        System.out.println("Books are Inserted Successfully");
    }

    /////////////////////////////////////////////////////////////////////////////////////////////

    public ArrayList<Book> Search(String value, int choice) throws IOException, SAXException, ParseException {
        Document GetFile = XML_Builder.parse(new File("src\\Books.xml"));
        ArrayList<Book> All_Books_Resulted = new ArrayList<>();

        NodeList ChildrenList = GetFile.getDocumentElement().getChildNodes();


        value = value.toLowerCase(Locale.ROOT);
        for (int i = 0; i < ChildrenList.getLength(); ++i) {
            Node Child = ChildrenList.item(i);

            if (Child.getNodeType() == Node.ELEMENT_NODE) {
                Element found = (Element) Child;

                String ID = ((Element) Child).getAttributes().getNamedItem("ID").getNodeValue();

                String Author = found.getElementsByTagName("Author").item(0).getChildNodes().item(0).getNodeValue();

                String Title = found.getElementsByTagName("Title").item(0).getChildNodes().item(0).getNodeValue();
                //System.out.println(T);
                String Genre = found.getElementsByTagName("Genre").item(0).getChildNodes().item(0).getNodeValue();

                Double price = Double.parseDouble(found.getElementsByTagName("Price").item(0).getChildNodes().item(0).getNodeValue());

                String Publish_Date = found.getElementsByTagName("Publish_Date").item(0).getChildNodes().item(0).getNodeValue();
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(Publish_Date);
                String Description = found.getElementsByTagName("Description").item(0).getChildNodes().item(0).getNodeValue();

                if (choice == 1) {
                    if (value.equals(ID.toLowerCase(Locale.ROOT))) {
                        All_Books_Resulted.add(new Book(ID, Author, Title, Genre, price, date, Description));
                    }
                }
                if (choice == 2) {
                    if (value.equals(Author.toLowerCase(Locale.ROOT))) {
                        All_Books_Resulted.add(new Book(ID, Author, Title, Genre, price, date, Description));
                    }
                }
                if (choice == 3) {
                    if (value.equals(Title.toLowerCase(Locale.ROOT))) {
                        All_Books_Resulted.add(new Book(ID, Author, Title, Genre, price, date, Description));
                    }
                }
                if (choice == 4) {
                    if (value.equals(Genre.toLowerCase(Locale.ROOT))) {
                        All_Books_Resulted.add(new Book(ID, Author, Title, Genre, price, date, Description));
                    }
                }
                if (choice == 5) {
                    double Value = Double.parseDouble(value);
                    if (Value == price) {
                        All_Books_Resulted.add(new Book(ID, Author, Title, Genre, price, date, Description));
                    }
                }
                if (choice == 6) {

                    if (value.equals(Publish_Date.toLowerCase(Locale.ROOT))) {
                        All_Books_Resulted.add(new Book(ID, Author, Title, Genre, price, date, Description));
                    }
                }
                if (choice == 7) {
                    if (value.equals(Description)) {
                        All_Books_Resulted.add(new Book(ID, Author, Title, Genre, price, date, Description));
                    }
                }
            }
        }
        return All_Books_Resulted;
    }
    /////////////////////////////////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////////////////////////////////

    public void Delete(String ID) throws TransformerException, IOException, SAXException {
        XML_Document = XML_Builder.parse(new File("src\\Books.xml"));
        XML_Document_Root = (Element) XML_Document.getDocumentElement();
        NodeList BookList = XML_Document.getElementsByTagName("Book");
        Boolean flag = true;
        for (int i = 0; i < BookList.getLength(); ++i) {
            Node Book = BookList.item(i);
            if (Book.getNodeType() == Node.ELEMENT_NODE) {
                Element BookElement = (Element) Book;
                if (BookElement.getAttributeNode("ID").getValue().equals(ID)) {
                    XML_Document_Root.removeChild(Book);
                    System.out.println("Book is deleted Successfully");
                    flag = false;
                }
            }
        }
        if (flag) {
            System.out.println("Book is not Found");
        }
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(XML_Document);
        Result result = new StreamResult(F_XML);
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(source, result);
    }

    public void DeleteForUpdated(String ID) throws TransformerException, IOException, SAXException {
        XML_Document = XML_Builder.parse(new File("src\\Books.xml"));
        XML_Document_Root = (Element) XML_Document.getDocumentElement();
        NodeList BookList = XML_Document.getElementsByTagName("Book");
        for (int i = 0; i < BookList.getLength(); ++i) {
            Node Book = BookList.item(i);
            if (Book.getNodeType() == Node.ELEMENT_NODE) {
                Element BookElement = (Element) Book;
                if (BookElement.getAttributeNode("ID").getValue().equals(ID)) {
                    XML_Document_Root.removeChild(Book);
                }
            }
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(XML_Document);
        Result result = new StreamResult(F_XML);
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(source, result);
    }

    public void UpdateBook() throws IOException, SAXException, ParseException, TransformerException {
        Document GetFile = XML_Builder.parse(new File("src\\Books.xml"));
        ArrayList<Book> All_Books_Resulted = new ArrayList<>();

        NodeList ChildrenList = GetFile.getDocumentElement().getChildNodes();

        for (int i = 0; i < ChildrenList.getLength(); ++i) {
            Node Child = ChildrenList.item(i);

            if (Child.getNodeType() == Node.ELEMENT_NODE) {
                Element found = (Element) Child;

                String ID = ((Element) Child).getAttributes().getNamedItem("ID").getNodeValue();

                String Author = found.getElementsByTagName("Author").item(0).getChildNodes().item(0).getNodeValue();

                String Title = found.getElementsByTagName("Title").item(0).getChildNodes().item(0).getNodeValue();
                //System.out.println(T);
                String Genre = found.getElementsByTagName("Genre").item(0).getChildNodes().item(0).getNodeValue();

                Double price = Double.parseDouble(found.getElementsByTagName("Price").item(0).getChildNodes().item(0).getNodeValue());

                String Publish_Date = found.getElementsByTagName("Publish_Date").item(0).getChildNodes().item(0).getNodeValue();
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(Publish_Date);
                String Description = found.getElementsByTagName("Description").item(0).getChildNodes().item(0).getNodeValue();

                All_Books_Resulted.add(new Book(ID, Author, Title, Genre, price, date, Description));
            }
        }
        for (int i = 0; i < All_Books_Resulted.size(); ++i) {
            System.out.println("==================================================================================");
            System.out.println("================================= Book " + (i + 1) + " =========================================");
            All_Books_Resulted.get(i).Print_Book();
        }
        if (All_Books_Resulted.size() == 0)
            return;

        System.out.print("Choose Book number: ");
        int choice = input.nextInt();
        Book Updated_Book = All_Books_Resulted.get(choice - 1);

        if (choice >= 1 && choice <= All_Books_Resulted.size()) {
            UpdateChoice(Updated_Book);

        } else {
            System.out.println("Wrong INPUT");
        }

        DeleteForUpdated(Updated_Book.getID());

        Addtag(Updated_Book);
        if (!F_XML.exists()) {
            XML_Document.appendChild(XML_Document_Root);
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(XML_Document);
        Result result = new StreamResult(F_XML);
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(source, result);

    }

    public void UpdateChoice(Book Updated_Book) {
        System.out.println("==================================================================================");
        System.out.println("1-Change Author");
        System.out.println("2-Change Title");
        System.out.println("3-Change Genre");
        System.out.println("4-Change Price");
        System.out.println("5-Change Publish Date");
        System.out.println("6-Change Description");
        input.nextLine();
        String choice_2 = input.nextLine();
        System.out.println(choice_2);
        //String Choice_2 = input.nextLine();

        if (choice_2.equals("1")) {
            System.out.println("Enter New Author");
            String Author = "";
            String Letters = "abcdefghijklmnopqrstuvwxyz";
            while (true) {
                Author = input.next();
                System.out.println(Author);
                Author = Author.trim();
                String AuthorLower = Author.toLowerCase();
                boolean All_IN_Letters = true;
                for (int j = 0; j < AuthorLower.length(); ++j) {
                    String letter = String.valueOf(AuthorLower.charAt(j));
                    if (!Letters.contains(letter)) {
                        All_IN_Letters = false;
                        break;
                    }
                }
                if (Author != "" && !Author.equalsIgnoreCase("null") && All_IN_Letters) {
                    break;
                } else
                    System.out.println("Enter Author Name Again");
            }
            Updated_Book.setAuthor(Author);
        }

        else if (choice_2.equals("2")) {
            System.out.println("Enter New Title");
            String Title = "";
            while (true) {
                Title = input.nextLine();
                Title = Title.trim();
                System.out.println(Title);
                if (Title != "" && !Title.equalsIgnoreCase("null")) {
                    break;
                } else
                    System.out.println("Enter Title Name Again");
            }
            Updated_Book.setTitle(Title);
        }

        else if (choice_2.equals("3")) {
            ArrayList<String> List = new ArrayList<>();
            List.add("science");
            List.add("fiction");
            List.add("drama");
            System.out.print("Enter New Book Genre: ");
            String Genre = "";
            while (true) {
                Genre = input.nextLine();
                Genre = Genre.trim();
                if (Genre != "" && !Genre.equalsIgnoreCase("null") && List.contains(Genre.toLowerCase(Locale.ROOT))) {
                    break;
                } else
                    System.out.println("Enter Genre Name Again");
            }
            Updated_Book.setGenre(Genre);
        }

        else if (choice_2.equals("4")) {
            double Price = 0;
            Boolean Repeat = true;
            while (Repeat) {
                try {
                    System.out.print("Enter New Book Price: ");
                    Price = input.nextDouble();
                    Repeat = false;
                } catch (Exception excpt) {
                    System.out.println("Wrong Input, Enter Price Again");
                    input.next();
                }
            }
            Updated_Book.setPrice(Price);
        }

        else if (choice_2.equals("5")) {
            Boolean Repeat = true;
            String oldstring;
            Date date = new Date();
            Repeat = true;
            while (Repeat) {
                try {

                    System.out.print("Enter New Book Publish Date 'Year-Month-Day': ");
                    oldstring = input.nextLine();
                    date = new SimpleDateFormat("yyyy-MM-dd").parse(oldstring);
                    Repeat = false;
                } catch (Exception e) {
                    System.out.println("wrong date format");
                }
            }
            Updated_Book.setPublish_Date(date);
        }

        else if (choice_2.equals("6")) {
            System.out.print("Enter New Book Description: ");
            String Description = input.nextLine();
            Updated_Book.setDescription(Description);
        }
        else {
            System.out.println("Wrong INPUT");
        }
    }


    public void Sort() throws IOException, SAXException, ParseException, TransformerException {
        Document GetFile = XML_Builder.parse(new File("src\\Books.xml"));
        ArrayList<Book> All_Books_Resulted = new ArrayList<>();

        NodeList ChildrenList = GetFile.getDocumentElement().getChildNodes();


        for (int i = 0; i < ChildrenList.getLength(); ++i) {
            Node Child = ChildrenList.item(i);

            if (Child.getNodeType() == Node.ELEMENT_NODE) {
                Element found = (Element) Child;

                String ID = ((Element) Child).getAttributes().getNamedItem("ID").getNodeValue();

                String Author = found.getElementsByTagName("Author").item(0).getChildNodes().item(0).getNodeValue();

                String Title = found.getElementsByTagName("Title").item(0).getChildNodes().item(0).getNodeValue();
                //System.out.println(T);
                String Genre = found.getElementsByTagName("Genre").item(0).getChildNodes().item(0).getNodeValue();

                Double price = Double.parseDouble(found.getElementsByTagName("Price").item(0).getChildNodes().item(0).getNodeValue());

                String Publish_Date = found.getElementsByTagName("Publish_Date").item(0).getChildNodes().item(0).getNodeValue();
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(Publish_Date);
                String Description = found.getElementsByTagName("Description").item(0).getChildNodes().item(0).getNodeValue();
                All_Books_Resulted.add(new Book(ID, Author, Title, Genre, price, date, Description));
            }
        }
        System.out.println("Choose how to sort About: ");
        System.out.println("1-By ID");
        System.out.println("2-By Author");
        System.out.println("3-By Title");
        System.out.println("4-By Genre");
        System.out.println("5-By Price");
        System.out.println("6-By Publish Date");
        System.out.println("7-By Description");
        int c = input.nextInt();
        System.out.println("Sorting Type:");
        System.out.println("1- Ascendingly");
        System.out.println("2- Descendingly");
        int c2=input.nextInt();

        if (c == 1) {
            ArrayList<String> Values = new ArrayList<>();
            HashMap<Book, String> Map = new HashMap<>();
            for (int i = 0; i < All_Books_Resulted.size(); ++i) {
                Map.put(All_Books_Resulted.get(i), All_Books_Resulted.get(i).getID());
                Values.add(All_Books_Resulted.get(i).getID());
            }
            Collections.sort(Values);


            List<Map.Entry<Book, String>> list = new LinkedList<>(Map.entrySet());

            // Sort the list
            Collections.sort(list, new Comparator<Map.Entry<Book, String>>() {
                public int compare(Map.Entry<Book, String> o1,
                                   Map.Entry<Book, String> o2) {
                    return (o1.getValue()).compareTo(o2.getValue());
                }
            });
            ArrayList<Book> All_Books_Resulted_Sorted = new ArrayList<>();
            // put data from sorted list to hashmap
            HashMap<Book, String> SortedMap = new LinkedHashMap<>();
            for (Map.Entry<Book, String> aa : list) {
                SortedMap.put(aa.getKey(), aa.getValue());
                All_Books_Resulted_Sorted.add(aa.getKey());

            }

            if (c2==2)
                Collections.reverse(All_Books_Resulted_Sorted);

            for (int i = 0; i < All_Books_Resulted.size(); ++i) {
                DeleteForUpdated(All_Books_Resulted.get(i).getID());
            }
            for (int i = 0; i < All_Books_Resulted_Sorted.size(); ++i) {

                Addtag(All_Books_Resulted_Sorted.get(i));
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(XML_Document);
                Result result = new StreamResult(F_XML);
                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.transform(source, result);
            }
        }

        else if (c == 2) {
            ArrayList<String> Values = new ArrayList<>();
            HashMap<Book, String> Map = new HashMap<>();
            for (int i = 0; i < All_Books_Resulted.size(); ++i) {
                Map.put(All_Books_Resulted.get(i), All_Books_Resulted.get(i).getAuthor());
                Values.add(All_Books_Resulted.get(i).getAuthor());
            }
            Collections.sort(Values);


            List<Map.Entry<Book, String>> list = new LinkedList<>(Map.entrySet());

            // Sort the list
            Collections.sort(list, new Comparator<Map.Entry<Book, String>>() {
                public int compare(Map.Entry<Book, String> o1,
                                   Map.Entry<Book, String> o2) {
                    return (o1.getValue()).compareTo(o2.getValue());
                }
            });
            ArrayList<Book> All_Books_Resulted_Sorted = new ArrayList<>();
            // put data from sorted list to hashmap
            HashMap<Book, String> SortedMap = new LinkedHashMap<>();
            for (Map.Entry<Book, String> aa : list) {
                SortedMap.put(aa.getKey(), aa.getValue());
                All_Books_Resulted_Sorted.add(aa.getKey());

            }

            if (c2==2)
                Collections.reverse(All_Books_Resulted_Sorted);

            for (int i = 0; i < All_Books_Resulted.size(); ++i) {
                DeleteForUpdated(All_Books_Resulted.get(i).getID());
            }
            for (int i = 0; i < All_Books_Resulted_Sorted.size(); ++i) {

                Addtag(All_Books_Resulted_Sorted.get(i));
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(XML_Document);
                Result result = new StreamResult(F_XML);
                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.transform(source, result);
            }
        }

        else if (c == 3) {
            ArrayList<String> Values = new ArrayList<>();
            HashMap<Book, String> Map = new HashMap<>();
            for (int i = 0; i < All_Books_Resulted.size(); ++i) {
                Map.put(All_Books_Resulted.get(i), All_Books_Resulted.get(i).getTitle());
                Values.add(All_Books_Resulted.get(i).getTitle());
            }
            Collections.sort(Values);


            List<Map.Entry<Book, String>> list = new LinkedList<>(Map.entrySet());

            // Sort the list
            Collections.sort(list, new Comparator<Map.Entry<Book, String>>() {
                public int compare(Map.Entry<Book, String> o1,
                                   Map.Entry<Book, String> o2) {
                    return (o1.getValue()).compareTo(o2.getValue());
                }
            });
            ArrayList<Book> All_Books_Resulted_Sorted = new ArrayList<>();
            // put data from sorted list to hashmap
            HashMap<Book, String> SortedMap = new LinkedHashMap<>();
            for (Map.Entry<Book, String> aa : list) {
                SortedMap.put(aa.getKey(), aa.getValue());
                All_Books_Resulted_Sorted.add(aa.getKey());

            }

            if (c2==2)
                Collections.reverse(All_Books_Resulted_Sorted);

            for (int i = 0; i < All_Books_Resulted.size(); ++i) {
                DeleteForUpdated(All_Books_Resulted.get(i).getID());
            }
            for (int i = 0; i < All_Books_Resulted_Sorted.size(); ++i) {

                Addtag(All_Books_Resulted_Sorted.get(i));
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(XML_Document);
                Result result = new StreamResult(F_XML);
                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.transform(source, result);
            }
        }

        else if (c == 4) {
            ArrayList<String> Values = new ArrayList<>();
            HashMap<Book, String> Map = new HashMap<>();
            for (int i = 0; i < All_Books_Resulted.size(); ++i) {
                Map.put(All_Books_Resulted.get(i), All_Books_Resulted.get(i).getGenre());
                Values.add(All_Books_Resulted.get(i).getGenre());
            }
            Collections.sort(Values);


            List<Map.Entry<Book, String>> list = new LinkedList<>(Map.entrySet());

            // Sort the list
            Collections.sort(list, new Comparator<Map.Entry<Book, String>>() {
                public int compare(Map.Entry<Book, String> o1,
                                   Map.Entry<Book, String> o2) {
                    return (o1.getValue()).compareTo(o2.getValue());
                }
            });
            ArrayList<Book> All_Books_Resulted_Sorted = new ArrayList<>();
            // put data from sorted list to hashmap
            HashMap<Book, String> SortedMap = new LinkedHashMap<>();
            for (Map.Entry<Book, String> aa : list) {
                SortedMap.put(aa.getKey(), aa.getValue());
                All_Books_Resulted_Sorted.add(aa.getKey());

            }

            if (c2==2)
                Collections.reverse(All_Books_Resulted_Sorted);

            for (int i = 0; i < All_Books_Resulted.size(); ++i) {
                DeleteForUpdated(All_Books_Resulted.get(i).getID());
            }
            for (int i = 0; i < All_Books_Resulted_Sorted.size(); ++i) {

                Addtag(All_Books_Resulted_Sorted.get(i));
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(XML_Document);
                Result result = new StreamResult(F_XML);
                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.transform(source, result);
            }
        }

        else if (c == 5) {
            ArrayList<String> Values = new ArrayList<>();
            HashMap<Book, String> Map = new HashMap<>();
            for (int i = 0; i < All_Books_Resulted.size(); ++i) {
                Map.put(All_Books_Resulted.get(i), String.valueOf(All_Books_Resulted.get(i).getPrice()));
                Values.add(String.valueOf(All_Books_Resulted.get(i).getPrice()));
            }
            Collections.sort(Values);


            List<Map.Entry<Book, String>> list = new LinkedList<>(Map.entrySet());

            // Sort the list
            Collections.sort(list, new Comparator<Map.Entry<Book, String>>() {
                public int compare(Map.Entry<Book, String> o1,
                                   Map.Entry<Book, String> o2) {
                    return (o1.getValue()).compareTo(o2.getValue());
                }
            });
            ArrayList<Book> All_Books_Resulted_Sorted = new ArrayList<>();
            // put data from sorted list to hashmap
            HashMap<Book, String> SortedMap = new LinkedHashMap<>();
            for (Map.Entry<Book, String> aa : list) {
                SortedMap.put(aa.getKey(), aa.getValue());
                All_Books_Resulted_Sorted.add(aa.getKey());

            }

            if (c2==2)
                Collections.reverse(All_Books_Resulted_Sorted);

            for (int i = 0; i < All_Books_Resulted.size(); ++i) {
                DeleteForUpdated(All_Books_Resulted.get(i).getID());
            }
            for (int i = 0; i < All_Books_Resulted_Sorted.size(); ++i) {

                Addtag(All_Books_Resulted_Sorted.get(i));
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(XML_Document);
                Result result = new StreamResult(F_XML);
                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.transform(source, result);
            }
        }

        else if (c == 6) {
            ArrayList<String> Values = new ArrayList<>();
            HashMap<Book, String> Map = new HashMap<>();
            for (int i = 0; i < All_Books_Resulted.size(); ++i) {

                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String Date = df.format(All_Books_Resulted.get(i).getPublish_Date());
                Map.put(All_Books_Resulted.get(i), Date);
                Values.add(Date);
            }
            Collections.sort(Values);


            List<Map.Entry<Book, String>> list = new LinkedList<>(Map.entrySet());

            // Sort the list
            Collections.sort(list, new Comparator<Map.Entry<Book, String>>() {
                public int compare(Map.Entry<Book, String> o1,
                                   Map.Entry<Book, String> o2) {
                    return (o1.getValue()).compareTo(o2.getValue());
                }
            });
            ArrayList<Book> All_Books_Resulted_Sorted = new ArrayList<>();
            // put data from sorted list to hashmap
            HashMap<Book, String> SortedMap = new LinkedHashMap<>();
            for (Map.Entry<Book, String> aa : list) {
                SortedMap.put(aa.getKey(), aa.getValue());
                All_Books_Resulted_Sorted.add(aa.getKey());

            }

            if (c2==2)
                Collections.reverse(All_Books_Resulted_Sorted);

            for (int i = 0; i < All_Books_Resulted.size(); ++i) {
                DeleteForUpdated(All_Books_Resulted.get(i).getID());
            }
            for (int i = 0; i < All_Books_Resulted_Sorted.size(); ++i) {

                Addtag(All_Books_Resulted_Sorted.get(i));
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(XML_Document);
                Result result = new StreamResult(F_XML);
                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.transform(source, result);
            }
        }

        else if (c == 7) {
            ArrayList<String> Values = new ArrayList<>();
            HashMap<Book, String> Map = new HashMap<>();
            for (int i = 0; i < All_Books_Resulted.size(); ++i) {
                Map.put(All_Books_Resulted.get(i), All_Books_Resulted.get(i).getDescription());
                Values.add(All_Books_Resulted.get(i).getDescription());
            }
            Collections.sort(Values);


            List<Map.Entry<Book, String>> list = new LinkedList<>(Map.entrySet());

            // Sort the list
            Collections.sort(list, new Comparator<Map.Entry<Book, String>>() {
                public int compare(Map.Entry<Book, String> o1,
                                   Map.Entry<Book, String> o2) {
                    return (o1.getValue()).compareTo(o2.getValue());
                }
            });
            ArrayList<Book> All_Books_Resulted_Sorted = new ArrayList<>();
            // put data from sorted list to hashmap
            HashMap<Book, String> SortedMap = new LinkedHashMap<>();
            for (Map.Entry<Book, String> aa : list) {
                SortedMap.put(aa.getKey(), aa.getValue());
                All_Books_Resulted_Sorted.add(aa.getKey());

            }

            if (c2==2)
                Collections.reverse(All_Books_Resulted_Sorted);

            for (int i = 0; i < All_Books_Resulted.size(); ++i) {
                DeleteForUpdated(All_Books_Resulted.get(i).getID());
            }
            for (int i = 0; i < All_Books_Resulted_Sorted.size(); ++i) {

                Addtag(All_Books_Resulted_Sorted.get(i));
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(XML_Document);
                Result result = new StreamResult(F_XML);
                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.transform(source, result);
            }
            /////////////////////////////////////////////////////////////////////////////////////////////

        }

        System.out.println("Sorting is Saved To DataFile, Go and See");
    }
}

