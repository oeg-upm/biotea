//
// This file was pubmed.openAccess.jaxb.generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.06.04 at 07:58:30 PM BST 
//


package pubmed.openAccess.jaxb.generated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}object-id" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}label" minOccurs="0"/>
 *         &lt;element ref="{}caption" minOccurs="0"/>
 *         &lt;choice maxOccurs="unbounded" minOccurs="0">
 *           &lt;element ref="{}alt-text"/>
 *           &lt;element ref="{}long-desc"/>
 *           &lt;element ref="{}email"/>
 *           &lt;element ref="{}ext-link"/>
 *           &lt;element ref="{}uri"/>
 *         &lt;/choice>
 *         &lt;choice maxOccurs="unbounded">
 *           &lt;element ref="{}alternatives"/>
 *           &lt;element ref="{}chem-struct"/>
 *           &lt;element ref="{}graphic"/>
 *           &lt;element ref="{}media"/>
 *           &lt;element ref="{}preformat"/>
 *           &lt;element ref="{}textual-form"/>
 *         &lt;/choice>
 *         &lt;choice maxOccurs="unbounded" minOccurs="0">
 *           &lt;element ref="{}attrib"/>
 *           &lt;element ref="{}permissions"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *       &lt;attribute name="position" default="float">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *             &lt;enumeration value="anchor"/>
 *             &lt;enumeration value="float"/>
 *             &lt;enumeration value="margin"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="orientation" default="portrait">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *             &lt;enumeration value="portrait"/>
 *             &lt;enumeration value="landscape"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="content-type" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="specific-use" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "objectIds",
    "label",
    "caption",
    "altTextsAndLongDescsAndEmails",
    "alternativesAndChemStructsAndGraphics",
    "attribsAndPermissions"
})
@XmlRootElement(name = "chem-struct-wrap")
public class ChemStructWrap {

    @XmlElement(name = "object-id")
    protected List<ObjectId> objectIds;
    protected Label label;
    protected Caption caption;
    @XmlElements({
        @XmlElement(name = "email", type = Email.class),
        @XmlElement(name = "uri", type = Uri.class),
        @XmlElement(name = "long-desc", type = LongDesc.class),
        @XmlElement(name = "ext-link", type = ExtLink.class),
        @XmlElement(name = "alt-text", type = AltText.class)
    })
    protected List<Object> altTextsAndLongDescsAndEmails;
    @XmlElements({
        @XmlElement(name = "preformat", type = Preformat.class),
        @XmlElement(name = "chem-struct", type = ChemStruct.class),
        @XmlElement(name = "textual-form", type = TextualForm.class),
        @XmlElement(name = "media", type = Media.class),
        @XmlElement(name = "graphic", type = Graphic.class),
        @XmlElement(name = "alternatives", type = Alternatives.class)
    })
    protected List<Object> alternativesAndChemStructsAndGraphics;
    @XmlElements({
        @XmlElement(name = "permissions", type = Permissions.class),
        @XmlElement(name = "attrib", type = Attrib.class)
    })
    protected List<Object> attribsAndPermissions;
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String position;
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String orientation;
    @XmlAttribute(name = "content-type")
    @XmlSchemaType(name = "anySimpleType")
    protected String contentType;
    @XmlAttribute(name = "specific-use")
    @XmlSchemaType(name = "anySimpleType")
    protected String specificUse;

    /**
     * Gets the value of the objectIds property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the objectIds property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getObjectIds().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ObjectId }
     * 
     * 
     */
    public List<ObjectId> getObjectIds() {
        if (objectIds == null) {
            objectIds = new ArrayList<ObjectId>();
        }
        return this.objectIds;
    }

    /**
     * Gets the value of the label property.
     * 
     * @return
     *     possible object is
     *     {@link Label }
     *     
     */
    public Label getLabel() {
        return label;
    }

    /**
     * Sets the value of the label property.
     * 
     * @param value
     *     allowed object is
     *     {@link Label }
     *     
     */
    public void setLabel(Label value) {
        this.label = value;
    }

    /**
     * Gets the value of the caption property.
     * 
     * @return
     *     possible object is
     *     {@link Caption }
     *     
     */
    public Caption getCaption() {
        return caption;
    }

    /**
     * Sets the value of the caption property.
     * 
     * @param value
     *     allowed object is
     *     {@link Caption }
     *     
     */
    public void setCaption(Caption value) {
        this.caption = value;
    }

    /**
     * Gets the value of the altTextsAndLongDescsAndEmails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the altTextsAndLongDescsAndEmails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAltTextsAndLongDescsAndEmails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Email }
     * {@link Uri }
     * {@link LongDesc }
     * {@link ExtLink }
     * {@link AltText }
     * 
     * 
     */
    public List<Object> getAltTextsAndLongDescsAndEmails() {
        if (altTextsAndLongDescsAndEmails == null) {
            altTextsAndLongDescsAndEmails = new ArrayList<Object>();
        }
        return this.altTextsAndLongDescsAndEmails;
    }

    /**
     * Gets the value of the alternativesAndChemStructsAndGraphics property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the alternativesAndChemStructsAndGraphics property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAlternativesAndChemStructsAndGraphics().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Preformat }
     * {@link ChemStruct }
     * {@link TextualForm }
     * {@link Media }
     * {@link Graphic }
     * {@link Alternatives }
     * 
     * 
     */
    public List<Object> getAlternativesAndChemStructsAndGraphics() {
        if (alternativesAndChemStructsAndGraphics == null) {
            alternativesAndChemStructsAndGraphics = new ArrayList<Object>();
        }
        return this.alternativesAndChemStructsAndGraphics;
    }

    /**
     * Gets the value of the attribsAndPermissions property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the attribsAndPermissions property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAttribsAndPermissions().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Permissions }
     * {@link Attrib }
     * 
     * 
     */
    public List<Object> getAttribsAndPermissions() {
        if (attribsAndPermissions == null) {
            attribsAndPermissions = new ArrayList<Object>();
        }
        return this.attribsAndPermissions;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the position property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPosition() {
        if (position == null) {
            return "float";
        } else {
            return position;
        }
    }

    /**
     * Sets the value of the position property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPosition(String value) {
        this.position = value;
    }

    /**
     * Gets the value of the orientation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrientation() {
        if (orientation == null) {
            return "portrait";
        } else {
            return orientation;
        }
    }

    /**
     * Sets the value of the orientation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrientation(String value) {
        this.orientation = value;
    }

    /**
     * Gets the value of the contentType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Sets the value of the contentType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContentType(String value) {
        this.contentType = value;
    }

    /**
     * Gets the value of the specificUse property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSpecificUse() {
        return specificUse;
    }

    /**
     * Sets the value of the specificUse property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpecificUse(String value) {
        this.specificUse = value;
    }

}