<?xml version="1.0"?>
<xsl:stylesheet version="2.0"
                xmlns:osm="http://www.openstreetmap.org/"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
                xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#"
                xmlns:prov="http://www.w3.org/ns/prov#"
                xmlns:osmdm="https://wiki.openstreetmap.org/wiki/"
                xmlns:dc="http://purl.org/dc/terms/">
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
                 xmlns:dc="http://purl.org/dc/terms/">

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
        <!--the rdf Description for each version -->
        <rdf:Description rdf:about="http://www.semanticweb.org/bernardroper/ontologies/2018/7/osmp#{name()}{@id}v{@version}">
            <!--variable to get the user who made this version -->
            <xsl:variable name="uid" select="@uid"/>
            <!--variable to store the changeset id-->
            <xsl:variable name="changeset" select="@changeset"/>
            <!--variable to store the  version-->
            <xsl:variable name="version" select="@version"/>
            <!-- for every attribute we find...-->
            <xsl:for-each select="@*">
                <xsl:attribute name="osm:{name()}"><xsl:value-of select="."/></xsl:attribute>
                <!--...apply a template that processes the attributes and turns them into qnamed attributes-->
                <!--&lt;!&ndash;if there are child nodes...&ndash;&gt;-->
                <!--<xsl:apply-templates select="child::*"/>-->

                <!--if the attribute is an id we make an attribution statement -->
                <!--<xsl:if test="name()='id'"> &lt;!&ndash;ON weds start here&ndash;&gt;-->
                    <!--<xsl:attribute name="prov:attributedTo">osm:users/<xsl:value-of select="$uid"/></xsl:attribute>-->
                    <!--&lt;!&ndash;<xsl:element name="prov:attributedTo">osm:users/<xsl:value-of select="$uid"/></xsl:element>&ndash;&gt;-->
                <!--</xsl:if>-->
            </xsl:for-each>

            <!-- handle tags and members-->
            <!--for every child element in the data...-->
            <xsl:for-each select="child::*">
                <!--make an element with same name prefixed with osm:-->
                <xsl:element name="osm:{name()}">
                   <!--if it is an 'nd' element, add an rdf:resource of type node-->
                    <xsl:if test="name()='nd'">
                        <xsl:attribute name="rdf:resource">http://www.openstreetmap.org/node/<xsl:value-of select="attribute()"/></xsl:attribute>
                    </xsl:if> <!-- !!!!!!!!!!!we need to handle member elements in relations as well!!!!!!!!!!!! -->
<!-- for every  attribute of a child element -->
                    <xsl:for-each select="attribute::*">
                        <!-- if it's a k prefix with : v - we need to add a uri prefix at some stage...-->
                        <xsl:if test="name()='k'">:</xsl:if>
                        <!--if it is a k or v attribute, put the value in the element this could be a
                        problem if we look at anything that isn't a k or a v attribute - that will not end up in the element-->
                        <!--<xsl:if test="name()='k' or name()='v'"> so we will probs not use this-->
                        <!--alternatively if the value isn't 'ref' then put in in the element-->
                        <xsl:if test="name()!='ref'">
                        <xsl:value-of select="self::node()"/>
                    </xsl:if>
                        <!-- if it's a 'k', then suffix with a %3D (slash) to separate it from 'v'-->
                        <xsl:if test="name()='k'">%3D</xsl:if>
                    </xsl:for-each>
                </xsl:element>
            </xsl:for-each>

            <!-- the prov attribution element - we still haven't minted the URI properly!-->
            <xsl:element name="prov:wasAttributedTo">
                <xsl:attribute name="rdf:resource">http://www.openstreetmap.org/users/<xsl:value-of select="$uid"/></xsl:attribute>
            </xsl:element>

            <!-- the rdf:type element - points at a class in the osmp ontology that subclasses a prov-o Entity
            the uri is a page on the osm wiki which has the canonical description-->
            <!--<rdfs:type rdf:resource="https://wiki.openstreetmap.org/wiki/{name()}"/>-->
           <!--in order to keep our ontology classes named nicely we uppercase the primitive's name here -->
            <xsl:choose>
                <xsl:when test="name()='node'">
                    <rdf:type rdf:resource="https://wiki.openstreetmap.org/wiki/Node"/>
                </xsl:when>
                <xsl:when test="name()='way'">
                    <rdf:type rdf:resource="https://wiki.openstreetmap.org/wiki/Way"/>
                </xsl:when>
                <xsl:when test="name()='relation'">
                    <rdf:type rdf:resource="https://wiki.openstreetmap.org/wiki/Relation"/>
                </xsl:when>
            </xsl:choose>
            <!--the prov wasRevisionOf element - points at the previous version-->
            <xsl:if test="$version > 1">
            <xsl:element name="prov:wasRevisionOf">
                <xsl:attribute name="rdf:resource">http://www.semanticweb.org/bernardroper/ontologies/2018/7/osmp#<xsl:value-of select="attribute::id"/>v<xsl:value-of select="$version - 1"/></xsl:attribute>
            </xsl:element>
        </xsl:if>
            <!--the prov generated by element - points at a changeset-->
            <xsl:element name="prov:wasGeneratedBy">
                <xsl:attribute name="rdf:resource">http://www.openstreetmap.org/changeset/<xsl:value-of select="$changeset"/></xsl:attribute>
            </xsl:element>
            <!--the dc isVersionOf by element points at the live primitive on the map-->
        <!--<xsl:element name="dc:isVersionOf"><xsl:attribute name="rdf:resource">http://www.openstreetmap.org/<xsl:value-of select="name()"/> </xsl:attribute></xsl:element>-->
            <xsl:apply-templates select="@id"/>
        </rdf:Description>
        <!-- make RDF descriptions for users -->
        <xsl:apply-templates select="@uid"/>
    </xsl:template>
    <!--when we see a user id we make an agent to attribute it to.-->
    <!--This agent is outside the osm primitive - we will end up with -->
    <!--lots of duplicates in a file, but only one in the triplestore-->
    <!--as they will overwrite each other-->
<xsl:template match="@uid">
    <rdf:Description rdf:about="http://www.openstreetmap.org/users/{.}"><rdfs:type rdf:resource="http://www.w3.org/ns/prov#Agent"/></rdf:Description>
</xsl:template>

    <xsl:template match="@id">
        <xsl:element name="dc:isVersionOf">
            <xsl:attribute name="rdf:resource">http://www.openstreetmap.org/<xsl:value-of select="parent::*/name()"/>/<xsl:value-of select="self::node()"/>
            </xsl:attribute></xsl:element>
    </xsl:template>

</xsl:stylesheet>