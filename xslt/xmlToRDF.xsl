<xsl:stylesheet
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
        xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
        xmlns:osm="http://www.openstreetmap.org/"
        xmlns:osmd="https://wiki.openstreetmap.org/wiki/Elements#"
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

        <xsl:apply-templates select="/*/*"/>

    </xsl:template>

    <!--process every child node (way, relation, node)-->
    <xsl:template match="/*/*">
        <rdf:Description rdf:about="https://openstreetmap.org/{name()}/{@id}/v{@version}">
            <!--<xsl:element name="pa:versionOf">https://www.openstreetmap.org/<xsl:value-of select="name()"/>/<xsl:value-of select="@id"/>-->
            <!--</xsl:element>-->
            <pa:versionOf rdf:resource="https://openstreetmap.org/{name()}/{@id}"/>
            <!--apply a template that processes the attributes and turns them into elements-->
            <xsl:apply-templates select="@*"/>
            <!--if there are child nodes...-->
            <xsl:apply-templates select="child::*"/>

        </rdf:Description>
    </xsl:template>

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
        <xsl:element name="osmd:member">https://www.openstreetmap.org/<xsl:value-of select="attribute::type"/>/<xsl:value-of
                select="attribute::ref"/>
        </xsl:element>
    </xsl:template>

    <xsl:template match="child::nd">
        <osmd:nd rdf:resource="https://www.openstreetmap.org/node/{attribute::ref}"/>
        <!--<xsl:element name="osmd:nd">https://www.openstreetmap.org/node/<xsl:value-of select="attribute::ref"/>-->
        <!--</xsl:element>-->
    </xsl:template>

    <xsl:template match="child::tag">
        <xsl:element name="osmd:tag">
            <xsl:value-of select="attribute::k"/>:<xsl:value-of select="attribute::v"/>
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