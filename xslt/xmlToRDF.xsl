<xsl:stylesheet
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
        xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
        xmlns:osm="http://www.openstreetmap.org/"
        xmlns:pa="https://www.ecs.soton.ac.uk/people/bar1g16/provanalytics#">


    <xsl:output method="xml" indent="yes"/>
    <!--Handle the document root  (<osm>)
    ...put an RDF tag in it-->
    <xsl:template match="/">
        <rdf:RDF>
            <xsl:apply-templates/>
        </rdf:RDF>
    </xsl:template>

    <!--handle the xml root (<osm>)
    put the precessed child tags in here -->
    <xsl:template match="/*">
        f
        <xsl:element name="{name()}"/>
        <xsl:apply-templates select="/*/*"/>
    </xsl:template>

<!--prcess every child node (way, relation, node)-->
    <xsl:template match="/*/*">
        <rdf:Description rdf:about="https://openstreetmap.org/{name()}/{@id}/v{@version}">
            <xsl:element name="pa:versionOf">https://openstreetmap.org/<xsl:value-of select="name()"/>/<xsl:value-of select="@id"/>
            </xsl:element>
            <xsl:apply-templates select="@*"/>
            <xsl:apply-templates select="child::*"/>
        </rdf:Description>
    </xsl:template>


<!--process attributes: make them into nodes containing their values-->
<xsl:template match="@*">
    <xsl:element name="{name()}">poopy</xsl:element>
</xsl:template>

    <xsl:template match="child::*">
        <xsl:element name="{name()}">wibble</xsl:element>
<!--<xsl:apply-templates select="@*"/>-->
    </xsl:template>



</xsl:stylesheet>