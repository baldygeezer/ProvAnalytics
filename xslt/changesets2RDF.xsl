<?xml version="1.0"?>
<xsl:stylesheet version="2.0"
                xmlns:osm="http://www.openstreetmap.org/"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
                xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#"
                xmlns:prov="http://www.w3.org/ns/prov#"
                xmlns:osmdm="https://wiki.openstreetmap.org/wiki/"
                xmlns:osmp="http://www.semanticweb.org/bernardroper/ontologies/2018/7/osmp#"
                xmlns:dc="http://purl.org/dc/terms/"
>
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
             xmlns:osmdm="https://wiki.openstreetmap.org/wiki/"
             xmlns:dc="http://purl.org/dc/terms/"
    >

        <xsl:apply-templates/>



    </rdf:RDF>
    </xsl:template>
    <!--process each changeset-->
    <xsl:template match="/*/*">

        <!--the rdf Description for each changeset -->
        <rdf:Description rdf:about="http://www.openstreetmap.org/changeset/{@id}v{@version}">

            <!--variable to get the user who opened this changeset -->
            <xsl:variable name="uid" select="@uid"/>
            <!-- for every attribute we find...-->
            <xsl:for-each select="@*">
                <!--.process the attributes and turn them into qnamed attributes-->
                <xsl:attribute name="osm:{name()}"><xsl:value-of select="."/></xsl:attribute>
            </xsl:for-each>

            <!-- handle tags and members-->
            <!--for every child element in the data...
            (which will be a tag, but get the name just in case there is a surprise one day...-->
            <xsl:for-each select="child::*">
                <!--make an element with same name prefixed with osm:-->
                <xsl:element name="osmp:{name()}">
                    <!-- for every  attribute of a child element (tag)-->
                    <xsl:for-each select="attribute::*">
                        <!-- if it's a k prefix with : v - we need to add a uri prefix at some stage...-->
                        <xsl:if test="name()='k'">:</xsl:if>
                            <xsl:value-of select="self::node()"/>
                        <!-- if it's a 'k', then suffix with a %3D (slash) to separate it from 'v'-->
                        <xsl:if test="name()='k'">%3D</xsl:if>
                    </xsl:for-each>
                </xsl:element>

                <!--<xsl:for-each select="attribute::*">-->
                    <!--<xsl:if test="name()='v'">-->
                       <!---->
                    <!--</xsl:if>-->

                    <!--&lt;!&ndash;<xsl:value-of select="dff"/>&ndash;&gt;-->

                    <!--<xsl:if test="self::node()='source'">-->
                        <!--<xsl:value-of select="self::*"/>-->
                        <!--<xsl:apply-templates select="@v"/>-->
                        <!--&lt;!&ndash;<xsl:element name="prov:used">&ndash;&gt;-->
                            <!--&lt;!&ndash;<xsl:attribute name="rdf:resource">http://www.openstreetmap.org/users/</xsl:attribute>&ndash;&gt;-->
                        <!--&lt;!&ndash;</xsl:element>&ndash;&gt;-->
                    <!--</xsl:if>-->
                <!--</xsl:for-each>-->
            </xsl:for-each>


            <!--<xsl:for-each select="child::*">-->
            <!--&lt;!&ndash; for every  attribute of a child element (tag)&ndash;&gt;-->
            <!--<xsl:for-each select="attribute::*">-->
                <!--<xsl:if test="self::node()='source'">poopy</xsl:if>-->
            <!--</xsl:for-each>-->
            <!--</xsl:for-each>-->

            <!-- the prov attribution element - we still haven't minted the URI properly!-->
            <xsl:element name="prov:wasAssociatedWith">
                <xsl:attribute name="rdf:resource">http://www.openstreetmap.org/users/<xsl:value-of select="$uid"/></xsl:attribute>
            </xsl:element>



<!-- give each changeset a type -->
            <rdf:type rdf:resource="http://www.w3.org/ns/prov#Activity"/>


            <xsl:apply-templates select="tag"/>

            <!--<xsl:if test="/*/*/*/attribute()='source'">-->

<!--<xsl:apply-templates select="/*/*/*/@v"/>-->
<!--</xsl:if>-->
        </rdf:Description>

        </xsl:template>



    <xsl:template match="tag">
        <xsl:if test="attribute::k='source'">
            <xsl:variable name= "val" select="replace(attribute::v,' ','')"/>
            <xsl:for-each select="tokenize($val,';')">
                    <xsl:element name="prov:used">
                        <xsl:attribute name="rdf:resource">http://www.semanticweb.org/bernardroper/ontologies/2018/7/osmp#<xsl:value-of select="."/></xsl:attribute>
                    </xsl:element>
            </xsl:for-each>
             </xsl:if>


    </xsl:template>

    <!--<xsl:template match="tag">-->
        <!--<xsl:if test="attribute::k='source'">-->
            <!--<xsl:element name="prov:used">-->
                <!--<xsl:attribute name="rdf:resource">http://www.semanticweb.org/bernardroper/ontologies/2018/7/osmp#<xsl:value-of select="attribute::v"/> </xsl:attribute>-->
            <!--</xsl:element> </xsl:if>-->
    <!--</xsl:template>-->


<!--<xsl:template match="/*/*/*/@v">-->
<!--&lt;!&ndash;<xsl:variable name="src" select="self::node()"/>&ndash;&gt;-->
<!--&lt;!&ndash;<xsl:if test="self::node()='source'">&ndash;&gt;-->
<!--<xsl:element name="prov:used">-->
    <!--<xsl:attribute name="rdf:resource">http://www.openstreetmap.org/users/<xsl:value-of select="/*/*/*/@v"/> </xsl:attribute>-->
<!--</xsl:element>-->
<!--&lt;!&ndash;</xsl:if>&ndash;&gt;-->
<!--</xsl:template>-->


</xsl:stylesheet>