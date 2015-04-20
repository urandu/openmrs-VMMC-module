<%
    ui.decorateWith("kenyaui", "panel", [ heading: "VMMC Care" ])

    def dataPoints = []

    if (config.complete) {

            dataPoints << [ label: "ART start date", value: "Never" ]

    }

%>

<% if (config.complete) { %>
<div class="ke-stack-item">
    <table width="100%" border="0">
        <tr>

        </tr>
    </table>
</div>
<% } %>
<div class="ke-stack-item">
    <% dataPoints.each { print ui.includeFragment("kenyaui", "widget/dataPoint", it) } %>
</div>
<div class="ke-stack-item">
    <% if (activeVisit) { %>
    <button type="button" class="ke-compact" onclick="ui.navigate('${ ui.pageLink("kenyaemr", "regimenEditor", [ patientId: currentPatient.id, category: "ARV", appId: currentApp.id, returnUrl: ui.thisUrl() ]) }')">
        <img src="${ ui.resourceLink("kenyaui", "images/glyphs/edit.png") }" />
    </button>
    <% } %>


</div>