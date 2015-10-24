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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element ref="{}award-group" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}funding-statement" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}open-access" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "awardGroups",
    "fundingStatements",
    "openAccess"
})
@XmlRootElement(name = "funding-group")
public class FundingGroup {

    @XmlElement(name = "award-group")
    protected List<AwardGroup> awardGroups;
    @XmlElement(name = "funding-statement")
    protected List<FundingStatement> fundingStatements;
    @XmlElement(name = "open-access")
    protected OpenAccess openAccess;

    /**
     * Gets the value of the awardGroups property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the awardGroups property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAwardGroups().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AwardGroup }
     * 
     * 
     */
    public List<AwardGroup> getAwardGroups() {
        if (awardGroups == null) {
            awardGroups = new ArrayList<AwardGroup>();
        }
        return this.awardGroups;
    }

    /**
     * Gets the value of the fundingStatements property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fundingStatements property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFundingStatements().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FundingStatement }
     * 
     * 
     */
    public List<FundingStatement> getFundingStatements() {
        if (fundingStatements == null) {
            fundingStatements = new ArrayList<FundingStatement>();
        }
        return this.fundingStatements;
    }

    /**
     * Gets the value of the openAccess property.
     * 
     * @return
     *     possible object is
     *     {@link OpenAccess }
     *     
     */
    public OpenAccess getOpenAccess() {
        return openAccess;
    }

    /**
     * Sets the value of the openAccess property.
     * 
     * @param value
     *     allowed object is
     *     {@link OpenAccess }
     *     
     */
    public void setOpenAccess(OpenAccess value) {
        this.openAccess = value;
    }

}
