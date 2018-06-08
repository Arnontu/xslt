<xsl:transform version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">

		<root >
			<xsl:for-each select="/root/butters"> 
				<butters><xsl:value-of select="."/></butters>
			</xsl:for-each>
		</root>

	</xsl:template>
</xsl:transform>

