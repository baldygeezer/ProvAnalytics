<xsl:stylesheet
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="3.0"
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
        <rdf:RDF
                xmlns="https://www.ecs.soton.ac.uk/people/bar1g16/OSMProv#"
                xml:base="https://www.ecs.soton.ac.uk/people/bar1g16/OSMProv"
                xmlns:pa="https://www.ecs.soton.ac.uk/people/bar1g16/provanalytics#"
                xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
                xmlns:owl="http://www.w3.org/2002/07/owl#"
                xmlns:xml="http://www.w3.org/XML/1998/namespace"
                xmlns:osm="http://www.openstreetmap.org/"
                xmlns:xsd="http://www.w3.org/2001/XMLSchema#"
                xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#"
                xmlns:prov="http://www.w3.org/ns/prov#"
                xmlns:osmd="https://wiki.openstreetmap.org/wiki/Elements#">
            <!--import PROV-O-->
            <owl:Ontology rdf:about="https://www.ecs.soton.ac.uk/people/bar1g16/OSMProv">
                <owl:imports rdf:resource="http://www.w3.org/ns/prov-o-20130430"/>
            </owl:Ontology>
            <!--add some mapping types to OSM-->
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
        <xsl:for-each select="distinct-values(/*/*/@uid)">
        <!--make a user agent for each unique user id we find-->
            <owl:NamedIndividual rdf:about="https://openstreetmap.org/users/{.}">
                <rdf:type rdf:resource="http://www.w3.org/ns/prov#Agent"/>
            </owl:NamedIndividual>
        </xsl:for-each>
    </xsl:template>

    <!--process a child node (way, relation, node)-->
    <xsl:template match="/*/*">
        <xsl:variable name="vsn" select="@version - 1"/>
        <owl:NamedIndividual rdf:about="https://openstreetmap.org/{name()}/{@id}v{@version}">
            <!-- assign an owl type based on which OSM element we are looking at-->
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

            <!--if the version is >1 we need to point at the qualified revision-->
            <xsl:if test="@version > 1">
                <prov:qualifiedRevision
                        rdf:resource="https://www.ecs.soton.ac.uk/people/bar1g16/OSMProv#dv{name()}{@id}v{@version}v{$vsn}"/>
            </xsl:if>
            <!-- make the attribution to an agent we have created todo except that the qualifiedAttribution relationship should go
            to an attribution not an agent-->
            <prov:qualifiedAttribution rdf:resource="https://www.ecs.soton.ac.uk/people/bar1g16/OSMProv#attr{@id}"/>
            <!-- triple which says what the current map version is NB @todo we need to look at whether we need to handle the fact
            that not all nodes are displayed - we could reason this with OWL  -->
            <pa:versionOf rdf:resource="https://openstreetmap.org/{name()}/{@id}"/>
            <!--apply a template that processes the attributes and turns them into elements-->
            <xsl:apply-templates select="@*"/>
            <!--deal with any child nodes...-->
            <xsl:apply-templates select="child::*"/>
        </owl:NamedIndividual>

        <!--if the version number is greater than 1 then it is derived from a prior version
          in which case we make a derivation (qualified relation design pattern)-->
        <xsl:if test="@version > 1">
            <owl:NamedIndividual rdf:about="https://www.ecs.soton.ac.uk/people/bar1g16/OSMProv#dv{name()}{@id}v{@version}v{$vsn}">
                <rdf:type rdf:resource="http://www.w3.org/ns/prov#Revision"/>
                <prov:entity rdf:resource="https://openstreetmap.org/{name()}/{@id}v{$vsn}"/>
                <!--<prov:qualifiedDerivation rdf:resource="https://openstreetmap.org/{name()}/{@id}v{@version}"/>-->
            </owl:NamedIndividual>
        </xsl:if>
        <!--create an attribution-->
        <owl:NamedIndividual rdf:about="https://www.ecs.soton.ac.uk/people/bar1g16/OSMProv#attr{@id}">
            <rdf:type rdf:resource="http://www.w3.org/ns/prov#Attribution"/>
            <prov:agent rdf:resource="https://openstreetmap.org/users/{@uid}"/>
        </owl:NamedIndividual>

    </xsl:template>


    <!-- ********************************************
    This is an osm specific block
    *************************************************

    well it would be but we haven't sprted t out yet!

    ********************************************
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

    <xsl:template match="child::member">
        <osmd:member rdf:resource="https://www.openstreetmap.org/way/{attribute::ref}"/>
    </xsl:template>

    <xsl:template match="child::nd">
        <osmd:nd rdf:resource="https://www.openstreetmap.org/node/{attribute::ref}"/>
    </xsl:template>

    <xsl:template match="child::tag">
        <xsl:element name="osmd:tag">
            <xsl:value-of select="attribute::k"/>|<xsl:value-of select="attribute::v"/>
        </xsl:element>
    </xsl:template>

    <xsl:template match="attribute::changeset">
        <osmd:changeset rdf:resource="https://www.openstreetmap.org/changeset/{.}"/>
    </xsl:template>

    <xsl:template match="attribute::uid">
        <osmd:uid rdf:resource="https://www.openstreetmap.org/users/{.}"/>
    </xsl:template>


</xsl:stylesheet>