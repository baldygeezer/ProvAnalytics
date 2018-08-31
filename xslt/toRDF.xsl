<xsl:stylesheet version="1.0"
                xmlns:osm="http://www.osm.org#"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
                xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#"
             >

    <!--xsl directives -->
    <xsl:output method="xml" indent="yes"/>
    <!--***************************************************************-->

    <!--match and Handle the document root  (<osm>)
        ...put an RDF tag in it-->
    <xsl:template match="/">
        <rdf:RDF>
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

        <rdf:Description rdf:about="https://openstreetmap.org/{name()}/{@id}/v{@version}">
            <xsl:for-each select="@*">
                <xsl:attribute name="osm:{name()}">
                    <xsl:value-of select="."/>
                </xsl:attribute>
            <!--apply a template that processes the attributes and turns them into rdf attributes-->
            <!--&lt;!&ndash;if there are child nodes...&ndash;&gt;-->
            <!--<xsl:apply-templates select="child::*"/>-->
    </xsl:for-each>
            <rdfs:type rdf:resource="prov:Entity"/>
            <xsl:for-each select="descendant::node()">
                <xsl:element name="osm:{name()}"><xsl:value-of select="attribute::*"/></xsl:element>
<!--<xsl:apply-templates select="/*/*"/>-->
            </xsl:for-each>
        </rdf:Description>
    </xsl:template>

<!--<xsl:template match="/*/*">-->

    <!--<xsl:element name="osm:wibble"><xsl:value-of select="."/></xsl:element>-->
<!--</xsl:template>-->


</xsl:stylesheet>