<?xml version="1.0"?>
<xsl:stylesheet version="2.0"
                xmlns:osm="http://www.openstreetmap.org/"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
                xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#"
                xmlns:prov="http://www.w3.org/ns/prov#"
                xmlns:osmdm="https://wiki.openstreetmap.org/wiki/">
    <!--xsl directives -->
    <xsl:output method="xml" indent="yes"/>
    <!--***************************************************************-->

    <!--match and Handle the document root  (<osm>)
        ...put an RDF tag in it-->
    <xsl:template match="/">
        <rdf:RDF xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
                 xmlns:owl="http://www.w3.org/2002/07/owl#"
                 xmlns:osm="http://www.openstreetmap.org/"
                 xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#"
                 xmlns:prov="http://www.w3.org/ns/prov#"
                 xmlns:osmp="http://www.semanticweb.org/bernardroper/ontologies/2018/7/osmp#"
                 xmlns:osmdm="https://wiki.openstreetmap.org/wiki/">
            <xsl:apply-templates/>
        </rdf:RDF>
    </xsl:template>
    <!--***************************************************************-->

    <!--  put the precessed Top level element inside the RDF tag (ways relations nodes) etc -->
    <xsl:template match="/*">
        <xsl:apply-templates select="/*/*"/>
    </xsl:template>

    <!--process every top level element(way, relation, node)-->
    <xsl:template match="/*/*">
        <rdf:Description rdf:about="osm:{name()}/{@id}/v{@version}">
            <xsl:variable name="uid" select="@uid"/>
            <xsl:for-each select="@*">
                <xsl:attribute name="osm:{name()}"><xsl:value-of select="."/></xsl:attribute>
                <!--apply a template that processes the attributes and turns them into rdf attributes-->
                <!--&lt;!&ndash;if there are child nodes...&ndash;&gt;-->
                <!--<xsl:apply-templates select="child::*"/>-->
                <xsl:if test="name()='id'">
                    <xsl:attribute name="prov:attributedTo">osm:users/<xsl:value-of select="$uid"/></xsl:attribute>
                </xsl:if>
            </xsl:for-each>
            <rdfs:type rdf:resource="osdm:{name()}"/>
            <xsl:for-each select="child::*">
                <xsl:element name="osm:{name()}">
                    <xsl:for-each select="attribute::*">
                        <xsl:if test="name()='k'">:</xsl:if>
                        <xsl:value-of select="self::node()"/>
                        <xsl:if test="name()='k'">%3D</xsl:if>
                    </xsl:for-each>

                </xsl:element>
                <!--<xsl:apply-templates select="/*/*"/>-->
            </xsl:for-each>
        </rdf:Description>
        <!-- make RDF descriptions for changesets and users -->
        <xsl:apply-templates select="@uid"/>
    </xsl:template>
<xsl:template match="@uid">
    <rdf:Description rdf:about="osm:users/{.}"><rdfs:type rdf:resource="prov:Agent"/></rdf:Description>
</xsl:template>

    <!--<xsl:template match="/*/*">-->

    <!--<xsl:element name="osm:wibble"><xsl:value-of select="."/></xsl:element>-->
    <!--</xsl:template>-->


</xsl:stylesheet>