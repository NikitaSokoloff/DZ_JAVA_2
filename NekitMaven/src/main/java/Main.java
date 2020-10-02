import ru.pflb.mq.dummy.exception.DummyException;
import ru.pflb.mq.dummy.implementation.ConnectionImpl;
import ru.pflb.mq.dummy.implementation.ProducerImpl;
import ru.pflb.mq.dummy.interfaces.Connection;
import ru.pflb.mq.dummy.interfaces.Destination;
import ru.pflb.mq.dummy.interfaces.Producer;
import ru.pflb.mq.dummy.interfaces.Session;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/*
1) Вызвать mvn install:install-file -Dfile=D:\Projects\JavaProjects\NekitMaven\lib\dummy-connector.jar -DgroupId=test -DartifactId=dummy-connector -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true
Чтобы добавить в локальный репо (иначе библиотека из скоупа system не добавляется, мавену нужен пом палюбас)
2) Вызвать mvn package для сборки нашего jar
3) В target будет NekitMaven-1.0-SNAPSHOT-jar-with-dependencies.jar с нашим проектом и либами внутри
4) Вызвать из cmd  "java -jar .\NekitMaven-1.0-SNAPSHOT-jar-with-dependencies.jar %путь_к_созданному_файлу"
 */
public class Main {
    public static void main(String[] args) {
        String fileName = args[0];
        try (BufferedReader br = new BufferedReader(new FileReader(fileName));
             Connection connection = new ConnectionImpl();
             Session session = connection.createSession(true);
        ) {
            Destination destination = session.createDestination("myQueue");
            Producer producer = new ProducerImpl(destination);
            String line;
            while ((line = br.readLine()) != null) {
                producer.send(line);
                Thread.sleep(2000);
            }
        } catch (DummyException | InterruptedException | IOException e) {
            System.out.println("Whoops error:" + e);
        }
    }
}
