<xsl:transform version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template match="/">
		<butters>
		  <xsl:value-of select="/hello/world"/></butters>
	</xsl:template>
</xsl:transform>
