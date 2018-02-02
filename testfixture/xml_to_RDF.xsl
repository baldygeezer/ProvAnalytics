<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" >

    <xsl:output method="html"/>

    <xsl:template match="/">
        <html>
            <body>
                <xsl:apply-templates/>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="*">
        wibble

    </xsl:template>


    <xsl:template match="/ARTICLE/TITLE">
        <h1 align="center">
            wibblwoo
            <xsl:apply-templates/>
        </h1>
    </xsl:template>


    <xsl:template match="/ARTICLE/SECT">
        <h2>
            <xsl:apply-templates select="text()|B|I|U|DEF|LINK"/>
        </h2>
        <xsl:apply-templates select="SECT|PARA|LIST|NOTE"/>
    </xsl:template>

    <xsl:template match="/ARTICLE/SECT/SECT">
        <h2>
            <xsl:apply-templates select="text()|B|I|U|DEF|LINK"/>
        </h2>
        <xsl:apply-templates select="SECT|PARA|LIST|NOTE"/>
    </xsl:template>

    <xsl:template match="PARA">
        <p>
            <xsl:apply-templates/>
        </p>
    </xsl:template>


</xsl:stylesheet>