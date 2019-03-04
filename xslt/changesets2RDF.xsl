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
                <xsl:attribute name="osm:{name()}">
                    <xsl:value-of select="."/>
                </xsl:attribute>
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

            </xsl:for-each>

            <!-- prov association with the user who opened the changeset-->
            <xsl:element name="prov:wasAssociatedWith">
                <xsl:attribute name="rdf:resource">http://www.openstreetmap.org/users/<xsl:value-of select="$uid"/>
                </xsl:attribute>
            </xsl:element>

            <!-- give each changeset a type -->
            <rdf:type rdf:resource="http://www.w3.org/ns/prov#Activity"/>

            <!--apply the template to handle tags-->
            <xsl:apply-templates select="tag"/>

        </rdf:Description>

        <!--make entities for the sources and imagery used These are outside the changeset statement as they are separate entities that the changeset points at -->
        <xsl:for-each select="child::tag">
            <xsl:if test="(attribute::k='source') or (attribute::k='imagery_used')">
                <!-- ...Then we make a variable containing  the 'v' attribute value  -->
                <xsl:variable name="vl" select="attribute::v"/>

                <!-- split the value on the semicolon and strip non uri characters then iterate over the resulting list structure-->
                <xsl:for-each select="tokenize(replace($vl,'[()/.# ]',''),';')">
                    <!--make a prov:Entity out of each value-->
                    <rdf:Description rdf:about="http://www.semanticweb.org/bernardroper/ontologies/2018/7/osmp#{.}">
                        <rdf:type rdf:resource="http://www.w3.org/ns/prov#Entity"/>
                    </rdf:Description>
                </xsl:for-each>

            </xsl:if>

            <!--make a prov:Software agent from the created_by - This is outside the changeset statement as it is a separate entity that the changeset points at-->
            <xsl:if test="(attribute::k='created_by')">
                <rdf:Description
                        rdf:about="http://www.semanticweb.org/bernardroper/ontologies/2018/7/osmp#{replace(attribute::v,'[()/.# ]','')}">
                    <rdf:type rdf:resource="http://www.w3.org/ns/prov#SoftwareAgent"/>
                </rdf:Description>
            </xsl:if>

        </xsl:for-each>
    </xsl:template>


    <!--template to deal with tags; strips non-uri characters, splits attribute value on ";" and makes a prov:used triple of each split value-->
    <xsl:template match="tag">
        <!--if the there is a key 'source' or 'imagery_used'... -->
        <xsl:if test="(attribute::k='source') or (attribute::k='imagery_used')">
            <!-- ...Then we make a variable containing ALL the 'v' attribute values, after we stripped out all the whitespace -->
            <xsl:variable name="val" select="replace(attribute::v,'[()/.# ]','')"/>
            <!-- split on the semicolon and iterate over the resulting list structure -->
            <xsl:for-each select="tokenize($val,';')">
                <!--make a prov:used element out of each value-->
                <xsl:element name="prov:used">
                    <xsl:attribute name="rdf:resource">http://www.semanticweb.org/bernardroper/ontologies/2018/7/osmp#<xsl:value-of
                            select="."/>
                    </xsl:attribute>
                </xsl:element>
            </xsl:for-each>
        </xsl:if>

        <!--check if the 'k' attribute  is a "created by"-->
        <xsl:if test="(attribute::k='created_by')">
            <!-- make a prov:association, do the regex thing to clean the value-->
            <xsl:element name="prov:wasAssociatedWith">
                <xsl:attribute name="rdf:resource">http://www.semanticweb.org/bernardroper/ontologies/2018/7/osmp#<xsl:value-of
                        select="replace(attribute::v,'[()/.# ]','')"/>
                </xsl:attribute>
            </xsl:element>

        </xsl:if>

    </xsl:template>


</xsl:stylesheet>