package codemetropolis.toolchain.cdfreducer.model;

import codemetropolis.toolchain.cdfreducer.exceptions.CdfReducerReaderException;
import codemetropolis.toolchain.cdfreducer.exceptions.CdfReducerWriterException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CdfReducer {

    @XmlAttribute
    private String version;

    @XmlAttribute
    private String id;

    @XmlElementWrapper(name="resources")
    @XmlElement(name="constant")
    private List<Constant> resources = new ArrayList<>();

    public CdfReducer() {}

    public CdfReducer(String version, String id) {
        this.version = version;
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Constant> getResources() {
        return Collections.unmodifiableList(resources);
    }

    public void addResource(Constant resource) {
        resources.add(resource);
    }

    public Map<String, String> getResourceMap() {
        Map<String, String> result = new HashMap<>();
        for(Constant resource : resources) {
            result.put(resource.getId(), resource.getValue());
        }
        return result;
    }


    public static CdfReducer readFromXML(String CdfReducerFile) throws FileNotFoundException, CdfReducerReaderException {

        File file = new File(CdfReducerFile);
        if(!file.exists()) {
            throw new FileNotFoundException();
        }

        try {
            JAXBContext context = JAXBContext.newInstance(CdfReducer.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            CdfReducer CdfReducer = (CdfReducer) unmarshaller.unmarshal(file);
            return CdfReducer;
        } catch (JAXBException e) {
            throw new CdfReducerReaderException(e);
        }
    }

    public void writeToXML(String CdfReducerFile) throws CdfReducerWriterException {

        try {
            File file = new File(CdfReducerFile);
            JAXBContext context = JAXBContext.newInstance(CdfReducer.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(this, file);
        } catch (JAXBException e) {
            throw new CdfReducerWriterException(e);
        }

    }

}
