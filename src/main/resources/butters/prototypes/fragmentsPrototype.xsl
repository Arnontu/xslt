<?xml version="1.0" encoding="UTF-8"?>
<fragments xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    
    <XSLT>
        <xsl:attribute name="name">
            <xsl:value-of select="xpath selector"/>
        </xsl:attribute>
    
        <xsl:choose>    
            <xsl:when test="conditon"/>
         </xsl:choose>

        <xsl:copy-of select="xpath selector"/>
        <xsl:for-each select="xpath selector"/>       
        <xsl:if test="condition"/>
        <xsl:value-of select="xpath selector"/>
    </XSLT>    

    <XML>
        <element></element>
        <attribute></attribute>
        <text></text>
    </XML>

</fragments>

