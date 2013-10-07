

function showPreviewJson(jsonURI, pgName) {
	
	$("#jsonresponse").html("Sende Anfrage...");
	
	$.ajax({
		type:'get',
		url: jsonURI+"?name="+pgName,
	  	dataType: 'json',
		success: function(data) {
			playground = eval(data);	
			$("#jsonresponse").html("Spielfeldname: " + playground.name);							 
			drawPlaygroundSVG(playground);
		},
		error: function(data) {						 
			$("#jsonresponse").html("Kein Spielfeld gewählt");							  
		}				
	});
}

function setDirty() {
	$("#jsonresponse").html("Dirty");
}


function drawPlaygroundSVG(playground) {
		
	var svgNS = "http://www.w3.org/2000/svg";
	var svgGraph = document.getElementById("svgGraph"); 
	// Das sollte auch mir jQuery gehen

	while (svgGraph.lastChild) {
		svgGraph.removeChild(svgGraph.lastChild);
	}

	// Punkte zeichnen Teil 1
	for(var i=0; i< playground.paths.length;i++){
		var x = playground.paths[i].fromPoint.x;
		var y = playground.paths[i].fromPoint.y;
		var id = playground.paths[i].fromPoint.id;
		
	  drawPoint(x, y, id);
	}

	// Punkte zeichnen Teil 2
	for(var i=0; i< playground.paths.length;i++){
	  drawPoint(playground.paths[i].toPoint.x, playground.paths[i].toPoint.y, playground.paths[i].fromPoint.id);
	}
	
	// Pfade einzeichnen
	// var i in playground.paths
	for(var i=0; i< playground.paths.length;i++){
	  drawPath(svgGraph, playground.paths[i], i);
	}
}


function drawPoint(x, y, id) {

	circle = document.createElementNS("http://www.w3.org/2000/svg", "circle");
 	circle.setAttributeNS(null, 'id', "point"+id)
	circle.setAttributeNS(null, "cx", x);
    circle.setAttributeNS(null, "cy", y);

    circle.setAttributeNS(null, "r", 15);

    svgGraph.appendChild(circle);

}

function drawPath(svgGraph, path, i){

	var newLine = document.createElementNS('http://www.w3.org/2000/svg','line');
    newLine.setAttribute('id',"path"+path.id);
    newLine.setAttribute('x1',path.fromPoint.x);
    newLine.setAttribute('y1',path.fromPoint.y);
    newLine.setAttribute('x2',path.toPoint.x);
    newLine.setAttribute('y2',path.toPoint.y);
    $("#svgGraph").append(newLine);
}


/*
 * $(".btn-slide").click(function(){ $("#panel").slideToggle("slow");
 * $(this).toggleClass("active"); return false; });
 */

// ################## Points (AJAX) #######################

function deletePoint(pointID) {
	
	$.ajax({
		type:'get',
//	  	url:"http://localhost:9000/admin/playgrounds/deletePoint/"+pointID,
		url:"/admin/playgrounds/deletePoint/"+pointID,
	  	dataType: 'json',
		success: function(request) {
			// redraw the whole playground (expensive)
			drawPlaygroundSVG(eval(request));
			// update points table
			$("#"+pointID).remove();
		},
		error: function(request) {						 
			$("#jsonresponse").html("Kein Spielfeld gewählt");							  
		}				
	});
}

function createPoint() {
	
	$.ajax({
		type:'get',
//	  	url:"http://localhost:9000/admin/playgrounds/points/createPoint/"+$("#selectCreatePointX").val()+"/"+$("#selectCreatePointY").val(),
		url:"/admin/playgrounds/points/createPoint/"+$("#selectCreatePointX").val()+"/"+$("#selectCreatePointY").val(),
	  	dataType: 'json',
		success: function(request) {
			var id = eval(request);
			// add point to playground
			drawPoint($("#selectCreatePointX").val(), $("#selectCreatePointY").val(), id);
			// add point to table (build string)
			var newPoint0 = '<tr id="'+id+'"><td width="60">'+id+'</td><td width="60"><select>';			
			var newPoint1 = '';
			for(var i=1; i<1200; i++) {
				if(i==$("#selectCreatePointX").val()) {
					newPoint1 = newPoint1.concat('<option value="'+i+'" selected>'+i+'</option>');
				} else {
					newPoint1 = newPoint1.concat('<option value="'+i+'">'+i+'</option>');
				}
			}				
			var newPoint2 = '</select></td><td width="60"><select>';			
			var newPoint3 = '';
			for(var i=1; i<900; i++) {
				if(i==$("#selectCreatePointX").val()) {
					newPoint3 = newPoint3.concat('<option value="'+i+'" selected>'+i+'</option>');
				} else {
					newPoint3 = newPoint3.concat('<option value="'+i+'">'+i+'</option>');
				}
			}						
			var newPoint4 = '</select></td><td><input type="submit" class="btn" value="Save" onclick="savePoint('+id+')"></td><td><input type="submit" class="btn" value="Delete" onClick="deletePoint('+id+')"></td></tr>';
			var result = newPoint0+newPoint1+newPoint2+newPoint3+newPoint4;
			// remove and append the row for point creation
			$(result).insertBefore('#createRow');
		},
		error: function(request) {						 
			$("#jsonresponse").html("Kein Spielfeld gewählt");							  
		}				
	});
}

function savePoint(pointID) {
	
	var x = $("#pointSelectX_"+pointID).val();
	var y = $("#pointSelectY_"+pointID).val();
	
	$.ajax({
		type:'get',
//	  	url:"http://localhost:9000/admin/playgrounds/points/savePoint/"+pointID+"/"+x+"/"+y,
	  	url:"/admin/playgrounds/points/savePoint/"+pointID+"/"+x+"/"+y,
	  	dataType: 'json',
		success: function(request) {
			// redraw the whole playground
			drawPlaygroundSVG(eval(request));
			// update points table is not necessary
		},
		error: function(request) {						 
			$("#jsonresponse").html("Kein Spielfeld gewählt");							  
		}
	});
	
}

// ################## Points (AJAX) #######################

function deletePath(pathID) {
	
	$.ajax({
		type:'get',
//	  	url:"http://localhost:9000/admin/playgrounds/paths/deletePath/"+pathID,
		url:"/admin/playgrounds/paths/deletePath/"+pathID,
	  	dataType: 'json',
		success: function(request) {
			// redraw the whole playground (expensive)
			drawPlaygroundSVG(eval(request));
			// update points table
			$("#"+pathID).remove();
		},
		error: function(request) {						 
			$("#jsonresponse").html("Kein Spielfeld gewählt");							  
		}				
	});
}

function createPath(pgName) {
	
	$.ajax({
		type:'get',
	  	url: "/admin/playgrounds/paths/createPath/"+$("#selectFrom").val()+"/"+$("#selectTo").val(),
	  	dataType: 'json',
		success: function(request) {
			var id = eval(request);
			// redraw all
			showPreviewJson(pgName);
			// add path to table (build string)
			var newPoint0 = '<tr id="'+id+'"><td width="60">'+id+'</td><td width="60"><select>';			
			var newPoint1 = '';
			for(var i=1; i<1200; i++) {
				if(i==$("#selectFrom").val()) {
					newPoint1 = newPoint1.concat('<option value="'+i+'" selected>'+i+'</option>');
				} else {
					newPoint1 = newPoint1.concat('<option value="'+i+'">'+i+'</option>');
				}
			}				
			var newPoint2 = '</select></td><td width="60"><select>';			
			var newPoint3 = '';
			for(var i=1; i<900; i++) {
				if(i==$("#selectTo").val()) {
					newPoint3 = newPoint3.concat('<option value="'+i+'" selected>'+i+'</option>');
				} else {
					newPoint3 = newPoint3.concat('<option value="'+i+'">'+i+'</option>');
				}
			}						
			var newPoint4 = '</select></td><td><input type="submit" class="btn" value="Save" onclick="savePath('+id+')"></td><td><input type="submit" class="btn" value="Delete" onClick="deletePath('+id+')"></td></tr>';
			var result = newPoint0+newPoint1+newPoint2+newPoint3+newPoint4;
			// remove and append the row for point creation
			$(result).insertBefore('#createRow');
		},
		error: function(request) {						 
			$("#jsonresponse").html("Kein Spielfeld gewählt");							  
		}				
	});
}

function savePath(pathID) {
	
	var fromID = $("#fromPoint_"+pathID).val();
	var toID = $("#toPoint_"+pathID).val();
	
	$.ajax({
		type:'get',
	  	url:"http://localhost:9000/admin/playgrounds/paths/savePath/"+pathID+"/"+fromID+"/"+toID,
	  	dataType: 'json',
		success: function(request) {
			// redraw the whole playground
			drawPlaygroundSVG(eval(request));
			// update points table is not necessary
		},
		error: function(request) {						 
			$("#jsonresponse").html("Kein Spielfeld gewaehlt");							  
		}
	});	
}

// ################## Points (Websocket) #######################

/*
 * Initiiert den Websocket zur Administration des Spielfeldes.
 */
function initPlaygroundSocket(wsURI) {
	
	console.log("Initialize websocket 4 playground ...");
	
	// Prüft, ob Websocket-Unterstützung für Mozilla- oder IE-Browser
	var WS = window['MozWebSocket'] ? MozWebSocket : WebSocket
	var playgroundSocket = new WS(wsURI)
	
// var playgroundSocket = $.websocket(wsURI);
	
	// Initiieren des Zustands auf dem Client (Laden des Spielfelds)
	var playground;
	
    // Empfangen heißt Anzeigen des Spielfeldes, welches als Json-Dokument vom
	// Server (oder einem Client???) verschickt wird.
    var receiveEvent = function(event) {
    	
    	var data = JSON.parse(event.data);
        
        // Fehler darstellen und Websocket schließen
        if(data.error) {
            playgroundSocket.close();
            // $("#websocketInfo span").text(data.error)
            // $("#websocketInfo").show()
            return
        } else {
            $("#websocketInfo").html('Websocket Success');
        }
    	
    	// TODO stefan (Real World): Feststellen, ob der Request nicht von
		// demselben Clienten geschickt wurde.
    	// if(playground!=data) {
	    	// Clientseitigen Zustand aktualisieren.
	    	playground = data;
	    	// Zeichne Spielfelder
	    	drawPlaygroundSVG(playground);
	    	// PointForm und PfadForm aktualisieren
	        updatePointForm(playground);
	        updatePathForm(playground);
    	// }
    } 
    
	// Senden der änderten Spielfelddaten an den Server über den Websocket
	// -Kanal.
    $("#submitSavePointsWS").click(function() {
    	playgroundSocket.send($.toJSON(playground));
    });
    $("#submitSavePathsWS").click(function() {
		playgroundSocket.send(playground)
    });
    
	// Wenn eine Message vom Server geschickt wird, wird die Funktion
	// receiveEvent aufgrufen.
    playgroundSocket.onmessage = receiveEvent;
}

/*
 * Aktualisiert das Formular zum Bearbeiten der Punkte.
 */
function updatePointForm(playground) {
	
	// alle Punkte holen und sortieren
	var sortedPoints = getPointsSorted(playground);
	
	// alle Punkte löschen
	$('tr').remove('.pointRow');
	
	// für jeden Punkt
	for(var i = 0; i < sortedPoints.length; i++) {		
		// füge eine neue Zeile hinzu
		createPointRow(playground, sortedPoints[i]);
	}
}

/*
 * Gibt alle Punkte eines Spielfeldes sortiert zurück.
 */
function getPointsSorted(playground) {
	var allPoints = new Array();
	// flatten
	for(var i = 0; i<(playground.paths.length*2); i=i+2){
		var path = playground.paths[i/2];
		allPoints[i] = path.fromPoint;
		allPoints[i+1] = path.fromPoint;
	}
	// Sortieren
	allPoints.sort(function Numsort(point1, point2){
		return parseInt(point2.id)-parseInt(point1.id);
	});
	var sortedPoints = new Array();
	var j = 0;
	// Duplikate entfernen
	for(var i = 0; i < allPoints.length; i++){		
		if(sortedPoints.length == 0 || sortedPoints[j-1].id != allPoints[i].id){
			sortedPoints[j] = allPoints[i];
			j++;
		}
	}
	return sortedPoints;
}

/*
 * Erzeugt eine Reihe zur Bearbeitung von Punkten.
 */
function createPointRow(playground, point) {
	
	console.log("Erzeuge Punkt "+point.id+" für das Spielfeld "+playground.name);
	
	// add point to table (build string)
	// onchange="savePoint2Playground('+point.id+')"
	var newPoint0 = '<tr id="'+point.id+'" class="pointRow"><td width="60">'+point.id+'</td><td width="60"><select id="pointSelectX_'+point.id+'">';			
	var newPoint1 = '';
	for(var i=1; i<1200; i++) {
		if(i==point.x) 
			newPoint1 = newPoint1.concat('<option id=pointSelectX_'+point.id+'_'+i+' value="'+i+'" selected>'+i+'</option>');
		else
			newPoint1 = newPoint1.concat('<option id=pointSelectX_'+point.id+'_'+i+' value="'+i+'">'+i+'</option>');
	}				
	var newPoint2 = '</select></td><td width="60"><select id="pointSelectY_'+point.id+'">';			
	var newPoint3 = '';
	
	for(var i=1; i<900; i++) {
		if(i==point.y) {
			newPoint3 = newPoint3.concat('<option id=pointSelectY_'+point.id+'_'+i+' value="'+i+'" selected>'+i+'</option>');
		} else {
			newPoint3 = newPoint3.concat('<option id=pointSelectY_'+point.id+'_'+i+' value="'+i+'">'+i+'</option>');
		}
	}	
	
	var newPoint4 = '</select></td><td><input type="submit" class="btn" value="Delete" id="pointSubmitDelete_'+point.id+'"></td></tr>';
	var result = newPoint0+newPoint1+newPoint2+newPoint3+newPoint4;
	
	// insert the row for point creation
	$(result).insertAfter('#pointHeaderRow');
	
	// Handler zur Aktualisierung der X-Koordinate
	$('#pointSelectX_'+point.id).change(function() {
		console.log("Aktualisierung für der X-Koordinate für Punkt "+point.id+" für das Spielfeld "+playground.name);		
		// Clientseitiges Modell
		for(var i=0; i<playground.paths.length; i++) {
			var path = playground.paths[i];
			if(path.fromPoint.id==point.id){
				path.fromPoint.x = parseInt($('#pointSelectX_'+point.id).val());
			}
			if(path.toPoint.id==point.id){
				path.toPoint.y = parseInt($('#pointSelectX_'+point.id).val());
			}
		}
		// Dirty Client State
		$("#websocketInfo").html('Dirty');
		$("#jsonresponse").html("Sende Anfrage...");
	});
	
	// Handler zur Aktualisierung der Y-Koordinate
	$('#pointSelectY_'+point.id).change(function() {
		console.log("Aktualisierung für der Y-Koordinate für Punkt "+point.id+" für das Spielfeld "+playground.name);		
		// Clientseitiges Modell
		for(var i=0; i<playground.paths.length; i++) {
			var path = playground.paths[i];
			if(path.fromPoint.id==point.id){
				path.fromPoint.y = parseInt($('#pointSelectY_'+point.id).val());
			}
			if(path.toPoint.id==point.id){
				path.toPoint.y = parseInt($('#pointSelectY_'+point.id).val());
			}
		}
		// Dirty Client State
		$('#websocketInfo').html('Dirty');
	});
	
	// Handler zum Löschen eines Punktes
	$('#pointSubmitDelete_'+point.id).click(function() {
		/*
		 * Löscht jeden Pfad, der den übergebenen Punkt besitzt für das gegebene
		 * Spielfeld. Die anderen Spielfelder bleiben unberührt.
		 */
		for(var i = 0; i<playground.paths.length; i++) {
			var path = playground.paths[i];
			if(path != null && path.fromPoint.id==point.id)
				path = null;
			if(path != null && path.toPoint.id==point.id)
				path = null;
		}
		
		// View
		$("#"+point.id).remove();
		
		// Dirty Client State
		$('#websocketInfo').html('Dirty');
	});	
}

/*
 * Aktualisiert das Formular zur Bearbeitung von Pfaden.
 */
function updatePathForm(playground) {

	// alle Pfade holen und nach ID sortieren
	var allPaths = playground.paths;
	allPaths.sort(function Numsort(path1, path2){
		return parseInt(path2.id)-parseInt(path1.id);
	});
	
	// alle Pfade löschen
	$('tr').remove('.pathRow');
	$('tr').remove('#pathCreateRow');
	
	// Anlegen
	createPathCreateRow(playground);
	
	// für jeden Pfad
	for(var i = 0; i < allPaths.length; i++) {		
		// füge eine neue Zeile hinzu
		createPathRow(playground, allPaths[i]);
	}	
}

/*
 * Erzeugt eine Zeile zum Anlegen eines Pfades.
 */
function createPathCreateRow(playground) {
	
	// add path to table (build string)
	var newpath0 = '<tr id="pathCreateRow"  class="pathRow"><td></td><td><select id="pathSelectFrom">';			
	
	var newpath1 = '';	
	var sortedPoints = getPointsSorted(playground);
	for(var i=0; i<sortedPoints.length; i++) {
		var point = sortedPoints[i];
		newpath1 = newpath1.concat('<option value="'+point.id+'">'+point.id+'</option>');
	}			
	var newpath2 = '</select></td><td><select id="pathSelectTo">';		
	
	var newpath3 = '';
	var sortedPoints = getPointsSorted(playground);
	for(var i=0; i<sortedPoints.length; i++) {
		var point = sortedPoints[i];
		newpath3 = newpath3.concat('<option value="'+point.id+'">'+point.id+'</option>');
	}
							
	var newpath4 = '</select></td><td><input id="pathCreateSubmit" type="submit" class="btn" value="New"></td><td></td></tr>';
	var result = newpath0+newpath1+newpath2+newpath3+newpath4;
	
	// insert before the row for path creation
	$(result).insertAfter('#pathHeaderRow');
	
	// Handler zum Anlegen eines Pfades
	$("#pathCreateSubmit").click(function() {
		
		console.log("Anlegen eines Pfades für das Spielfeld "+playground.name);		
		
		// Clientseitiges Modell: Anfügen ans Ende
		
		var fromPoint = getPointById(playground, $("#pathSelectFrom").val());				
		var toPoint = getPointById(playground, parseInt($("#pathSelectTo").val()));
		
		var newIndex = playground.paths.length;
		playground.paths[newIndex] = new Object();
		// Eine neue ID, wird auf dem Server erzeugt (nur pro forma)
		playground.paths[newIndex]["id"] = 0;
		playground.paths[newIndex]["fromPoint"] = fromPoint;
		playground.paths[newIndex]["toPoint"] = toPoint;
		
		// View
		createPathRow(playground, playground.paths[newIndex]);
		
		// Dirty Client State
		$('#websocketInfo').html('Dirty');
	});	
}

function getPointById(playground, pointID) {	
	var sortedPoints = getPointsSorted(playground);
	for(var i=0; i < sortedPoints.length; i++)
		if(sortedPoints[i].id == pointID)
			return sortedPoints[i];
}

/*
 * Erzeugt eine Zeile zur Bearbeitung eines Pfades.
 */
function createPathRow(playground, path) {
	
	console.log("Erzeuge Pfad "+path.id+" für das Spielfeld "+playground.name);
	
	// add path to table (build string)
	var newpath0 = '<tr id="'+path.id+'"  class="pathRow"><td width="60">'+path.id+'</td><td width="60"><select id="pathSelectFrom_'+path.id+'">';			
	
	var newpath1 = '';	
	var sortedPoints = getPointsSorted(playground);
	for(var i=0; i<sortedPoints.length; i++) {
		var point = sortedPoints[i];
		if(point.id==path.fromPoint.id)
			newpath1 = newpath1.concat('<option value="'+point.id+'" selected>'+point.id+'</option>');
		else
			newpath1 = newpath1.concat('<option value="'+point.id+'">'+point.id+'</option>');
	}			
	var newpath2 = '</select></td><td width="60"><select id="pathSelectTo_'+path.id+'">';			
	
	var newpath3 = '';
	var sortedPoints = getPointsSorted(playground);
	for(var i=0; i<sortedPoints.length; i++) {
		var point = sortedPoints[i];
		if(point.id==path.toPoint.id)
			newpath3 = newpath3.concat('<option value="'+point.id+'" selected>'+point.id+'</option>');
		else
			newpath3 = newpath3.concat('<option value="'+point.id+'">'+point.id+'</option>');
	}
							
	var newpath4 = '</select></td><td><input type="submit" class="btn" value="Delete" id="pathSubmitDelete_'+path.id+'"></td></tr>';
	var result = newpath0+newpath1+newpath2+newpath3+newpath4;
	
	// insert before the row for path creation
	$(result).insertAfter('#pathHeaderRow');
	
	// Handler zur Aktualisierung des Startpunktes
	$('#pathSelectFrom_'+path.id).click(function() {
		console.log("Aktualisierung des Startpunktes für den Pfad "+path.id+" für das Spielfeld "+playground.name);		
		// Clientseitiges Modell:
		for(var i=0; i<playground.paths.length; i++) {
			var aPath = playground.paths[i];
			if(aPath.id==path.id){
				aPath.fromPoint = parseInt($('#pathSelectFrom_'+path.id).val());
			}			
		}
		// Dirty Client State
		$('#websocketInfo').html('Dirty');
	});
	
	// Handler zur Aktualisierung des Endpunktes
	$('#pathSelectTo_'+path.id).change(function() {
		console.log("Aktualisierung des Endpunktes für den Pfad "+path.id+" für das Spielfeld "+playground.name);		
		// Clientseitiges Modell:
		for(var i=0; i<playground.paths.length; i++) {
			var aPath = playground.paths[i];
			if(aPath.id==path.id){
				aPath.toPoint = parseInt($('#pathSelectTo_'+path.id).val());
			}
		}
		// Dirty Client State
		$('#websocketInfo').html('Dirty');
	});
	
	// Handler zum Löschen eines Pfades
	$('#pathSubmitDelete_'+path.id).click(function() {
		
		console.log("Löschen des Pfades "+path.id+" für das Spielfeld "+playground.name);
		
		// Modell
		for(var i = 0; i<playground.paths.length; i++) {
			var aPath = playground.paths[i];
			if(aPath != null && aPath.id==path.id)
				aPath = null;
			if(aPath != null && aPath.id==path.id)
				aPath = null;
		}
		
		// View
		$("#"+path.id).remove();
		
		// Dirty Client State
		$('#websocketInfo').html('Dirty');
	});
}