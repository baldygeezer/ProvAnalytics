<?xml version="1.0" ?>
<xsl:stylesheet
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
        xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
        xmlns:osm="http://www.openstreetmap.org/"
        xmlns:pa="https://www.ecs.soton.ac.uk/people/bar1g16/provanalytics#">
    <xsl:output method="xml" indent="yes" encoding="utf-8"/>
    <xsl:template match="/">
        <rdf:RDF>
            <xsl:apply-templates/>
        </rdf:RDF>
    </xsl:template>
    <!--handle the root node-->
    <xsl:template match="/*">
        <xsl:apply-templates/>
    </xsl:template>
    <!--Handle element nodes: i.e. nodes, ways, relations-->
    <xsl:template match="/*/*">
        <!-- rdf subject - this is the version item that will become a PROV entity
        This URL will not dereference to anything
        -->

        <rdf:Description rdf:about="https://openstreetmap.org/{name()}/{@id}/v{@version}">
            <xsl:element name="pa:versionOf">https://openstreetmap.org/<xsl:value-of select="name()"/>/<xsl:value-of select="@id"/>
            </xsl:element>
            <!--handle attributes of element nodes-->
            <xsl:template match="@* | node()">
                <!--for each attribute...-->
                <xsl:for-each select="attribute::*">
                    <!--make an element (predicate) using its name and place its value (object) inside-->
                    <xsl:element name="osm:{name()}">
                        <xsl:value-of select="."/>
                    </xsl:element>
                    <!--</xsl:for-each>-->

                    <!--<xsl:for-each select="child::*">-->
                    <!--<xsl:element name="osm:{name()}">-->


                    <!--&lt;!&ndash;<xsl:value-of select="attribute::*"/>&ndash;&gt;-->
                    <!--<xsl:value-of select="./@*"/>-->

                    <!--</xsl:element>-->

                </xsl:for-each>


                <!--<xsl:template match="node()/* | @*">-->
                <!--<xsl:copy>-->
                <!--<xsl:apply-templates select="node()/* | @*"/>-->
                <!--</xsl:copy>-->
                <!--</xsl:template>-->


            </xsl:template>

        </rdf:Description>
        <xsl:apply-templates select="/*/*/*"/>

    </xsl:template>

    <xsl:template match="/*/*/*">
        <xsl:apply-templates select="@*"/>
    </xsl:template>

    <!--template to turn attributes into nodes-->
    <xsl:template match="@*">
        <xsl:for-each select="attribute::*">
            <xsl:element name="ploppy">
                <xsl:value-of select="."/>
            </xsl:element>
        </xsl:for-each>
    </xsl:template>

</xsl:stylesheet>
