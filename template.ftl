<#ftl>
<#setting locale="pl">
<#if namingRecords?has_content>
    <#assign indent = "\t">
<#list namingRecords as nr>
${indent}${nr.getId()?string.computer} ${nr.tag}
<#assign indent = indent + "\t"> |__
 </#list>
</#if>
<#list lines as line>
    <#assign tag = line.tag>
    <#assign durationHours = line.getDuration().toPeriod().getHours()>
    <#assign durationTotalHours = line.getTotalDuration().toPeriod().getHours()>
    <#assign durationMinutes = line.getDuration().toPeriod().getMinutes()>
    <#assign durationTotalMinutes = line.getTotalDuration().toPeriod().getMinutes()>
    <#assign inserted = line.timestamp?string("HH:mm, EEE, MMM d ")>
    <#assign isSetDurationMinutes = (durationMinutes >= 0)>
    <#if isSetDurationMinutes>  
${line.count})${"\t"}${tag}${"\t"}${durationHours}:${durationMinutes} min / ${durationTotalHours}:${durationTotalMinutes}${"\t"}${inserted} id(${line.getId()?string.computer}) 
<#if line.description?has_content>
${"\t"}${line.description}
</#if>
    <#else>
No duration minutes set.
    </#if>
</#list> 