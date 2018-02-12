<xsl:stylesheet
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
        xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
        xmlns:osm="http://www.openstreetmap.org/"
        xmlns:osmd="https://wiki.openstreetmap.org/wiki/Elements#"
        xmlns:pa="https://www.ecs.soton.ac.uk/people/bar1g16/provanalytics#"
        xmlns:owl="http://www.w3.org/2002/07/owl#"
        xmlns:prov="http://www.w3.org/ns/prov#">


    <xsl:output method="xml" indent="yes"/>
    <!--Handle the document root  (<osm>)
    ...put an RDF tag in it-->
    <xsl:template match="/">
        <rdf:RDF xmlns="https://www.ecs.soton.ac.uk/people/bar1g16/OSMProv#"
                 xml:base="https://www.ecs.soton.ac.uk/people/bar1g16/OSMProv#"
                 xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
                 xmlns:owl="http://www.w3.org/2002/07/owl#"
                 xmlns:xml="http://www.w3.org/XML/1998/namespace"
                 xmlns:xsd="http://www.w3.org/2001/XMLSchema#"
                 xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#"
                 xmlns:prov="http://www.w3.org/ns/prov#">
            <owl:Ontology rdf:about="https://www.ecs.soton.ac.uk/people/bar1g16/OSMProv">
                <owl:imports rdf:resource="http://www.w3.org/ns/prov-o-20130430"/>
            </owl:Ontology>
            <owl:Class rdf:about="https://www.ecs.soton.ac.uk/people/bar1g16/OSMProv#Node">
                <rdfs:subClassOf rdf:resource="http://www.w3.org/ns/prov#Entity"/>
            </owl:Class>
            <owl:Class rdf:about="https://www.ecs.soton.ac.uk/people/bar1g16/OSMProv#Relation">
                <rdfs:subClassOf rdf:resource="http://www.w3.org/ns/prov#Collection"/>
            </owl:Class>
            <owl:Class rdf:about="https://www.ecs.soton.ac.uk/people/bar1g16/OSMProv#Way">
                <rdfs:subClassOf rdf:resource="http://www.w3.org/ns/prov#Collection"/>
            </owl:Class>
            <xsl:apply-templates/>
        </rdf:RDF>
    </xsl:template>

    <!--handle the xml root (<osm>)
    put the precessed child tags in here -->
    <xsl:template match="/*">

        <xsl:apply-templates select="/*/*"/>

    </xsl:template>

    <!--process every child node (way, relation, node)-->
    <xsl:template match="/*/*">
        <owl:NamedIndividual rdf:about="https://openstreetmap.org/{name()}/{@id}v{@version}">
            <!--<rdf:Description rdf:about="https://openstreetmap.org/{name()}/{@id}/v{@version}">-->
            <!--<xsl:apply-templates match="/*/way"/>-->
            <!--<prov:wasRevisionOf rdf:resource="https://openstreetmap.org/node/254429v1"/>-->
<xsl:element name="prov:wasRevisionOf" ><xsl:attribute name="rdf:resource">https://openstreetmap.org/{name()}/{@id</xsl:attribute></xsl:element>
            <xsl:choose>
                <xsl:when test="name()='way'">
                    <xsl:element
                            name="rdf:type">
                        <xsl:attribute name="rdf:resource">https://www.ecs.soton.ac.uk/people/bar1g16/OSMProv#Way</xsl:attribute>
                    </xsl:element>
                </xsl:when>

                <xsl:when test="name()='relation'">
                    <xsl:element
                            name="rdf:type">
                        <xsl:attribute name="rdf:resource">https://www.ecs.soton.ac.uk/people/bar1g16/OSMProv#Relation</xsl:attribute>
                    </xsl:element>
                </xsl:when>
                <xsl:when test="name()='node'">
                    <xsl:element
                            name="rdf:type">
                        <xsl:attribute name="rdf:resource">https://www.ecs.soton.ac.uk/people/bar1g16/OSMProv#Node</xsl:attribute>
                    </xsl:element>
                </xsl:when>

            </xsl:choose>

            <pa:versionOf rdf:resource="https://openstreetmap.org/{name()}/{@id}"/>
            <!--apply a template that processes the attributes and turns them into elements-->
            <xsl:apply-templates select="@*"/>
            <!--if there are child nodes...-->
            <xsl:apply-templates select="child::*"/>
        </owl:NamedIndividual>
        <!--</rdf:Description>-->
    </xsl:template>

    <!--<xsl:template match="/*/way">-->

    <!--<xsl:element name="rdf:type"><xsl:attribute name="resource">http://www.w3.org/ns/prov#Way</xsl:attribute></xsl:element>-->

    <!--</xsl:template>-->

    <!-- ********************************************
    This is an osm specific block
    ************************************************* -->
    <!--<xsl:template match="/*/*/tag">-->
    <!--<osm:tag>-->
    <!--<xsl:apply-templates select="@*"/>-->
    <!--</osm:tag>-->
    <!--</xsl:template>-->

    <!-- ********************************************
    End of osm specific block
    ************************************************* -->

    <!--process attributes: make them into nodes containing their values-->
    <xsl:template match="@*">
        <xsl:element name="osmd:{name()}">
            <xsl:value-of select="."/>
        </xsl:element>
    </xsl:template>

    <xsl:template match="child::*">
        <xsl:element name="{name()}">
            <xsl:apply-templates select="attribute::*"/>
        </xsl:element>
        <xsl:apply-templates select="child::member"/>
        <xsl:apply-templates select="child::nd"/>
        <xsl:apply-templates select="child::tag"/>
        <!--<xsl:apply-templates select="@*"/>-->
    </xsl:template>

    <!--<xsl:template match="attribute::*">-->
    <!--<xsl:value-of select="."/>        &lt;!&ndash;<xsl:apply-templates select="@*"/>&ndash;&gt;-->
    <!--</xsl:template>-->

    <xsl:template match="child::member">
        <!--<xsl:element name="osmd:member">https://www.openstreetmap.org/<xsl:value-of select="attribute::type"/>/<xsl:value-of-->
        <!--select="attribute::ref"/>-->
        <!--</xsl:element>-->
        <osmd:member rdf:resource="https://www.openstreetmap.org/way/{attribute::ref}"/>
    </xsl:template>

    <xsl:template match="child::nd">
        <osmd:nd rdf:resource="https://www.openstreetmap.org/node/{attribute::ref}"/>
        <!--<xsl:element name="osmd:nd">https://www.openstreetmap.org/node/<xsl:value-of select="attribute::ref"/>-->
        <!--</xsl:element>-->
    </xsl:template>

    <xsl:template match="child::tag">
        <xsl:element name="osmd:tag">
            <xsl:value-of select="attribute::k"/>|<xsl:value-of select="attribute::v"/>
        </xsl:element>
    </xsl:template>

    <xsl:template match="attribute::changeset">
        <osmd:changeset rdf:resource="https://www.openstreetmap.org/changeset/{.}"/>
        <!--<xsl:element name="osmd:changeset">https://www.openstreetmap.org/changeset/<xsl:value-of select="."/></xsl:element>-->
    </xsl:template>

    <xsl:template match="attribute::uid">
        <osmd:uid rdf:resource="https://www.openstreetmap.org/users/{.}"/>
        <!--<xsl:element name="osmd:uid">https://www.openstreetmap.org/users/<xsl:value-of select="."/></xsl:element>-->
    </xsl:template>


</xsl:stylesheet>