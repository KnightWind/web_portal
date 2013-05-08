/*jshint jquery: true, browser: true */
/*global Hash: false, Model: false, QueryString: false */
/*!
 * jQuery UI ThemeRoller client-side JavaScript file
 * http://jqueryui.com/themeroller/
 *
 * Copyright 2012 jQuery Foundation and other contributors
 * Released under the MIT license.
 * http://jquery.org/license
 */
(function( $, Hash, Model, QueryString, undefined ) {
	var model, reloadRollYourOwn, skipHashChange, theme, Theme,
		focusedEl = null,
		lastRollYourOwnLoad = 0,
		openGroups = [],
		themeroller = $( "#themeroller" ),
		baseVars = QueryString.decode( themeroller.data( "base-vars" ) ),
		downloadJqueryuiHost = themeroller.data( "download-jqueryui-host" ),
		imageGeneratorUrlPart = themeroller.data( "image-generator-url" );

	// Rewrite host for testing on staging
	if ( /^stage\./.test( location.host ) ) {
		downloadJqueryuiHost = downloadJqueryuiHost.replace( /(download\.)/, "stage.$1" );
	}

	// Returns imageGenerator url
	function imageGeneratorUrl( texturewidth, textureheight, value ) {
		return imageGeneratorUrlPart + "?new=555555&w=" + texturewidth + "&h=" + textureheight + "&f=png&q=100&fltr[]=over|textures/" + value + "|0|0|100";
	}

	// Fetches rollYourOwn content
	function rollYourOwnFetch() {
		return $.ajax( model.rollYourOwnUrl(), {
			dataType: "jsonp"
		});
	}

	function isHexColor( value ) {
		if ( (/[^#a-fA-F0-9]/g).test( value ) ) {
			return false;
		}
		if ( value.lastIndexOf( "#" ) !== 0 ) {
			// If # in any position but 0.
			return false;
		}
		if ( value.length !== 4 && value.length !== 7 ) {
			return false;
		}
		return true;
	}

	// Function to append a new theme stylesheet with the new style changes
	function updateCSS() {
		$( "body" ).append( "<link href=\"" + model.parsethemeUrl() + "\" type=\"text/css\" rel=\"Stylesheet\" />");
		var links = $( "link[href*=parsetheme\\.css]" );
		if ( links.length > 1 ) {
			// Wait a few seconds before removing previous theme(s) to avoid FOUW
			setTimeout(function() {
				links.not( ":last" ).remove();
			}, 5000 );
		}
	}

	// Function called after a change event in the form
	function formChange() {
		model.set( QueryString.decode( themeroller.find( ".application form" ).serialize() ) );
	}

	// Set up spindowns
	$.fn.spinDown = function() {
		return this.on( "click", function( event ) {
			var $this = $( this );

			$this.next().slideToggle( 100 );
			$this.find( ".arrow-icon" ).toggleClass( "icon-triangle-1-s" ).end().toggleClass( "state-active" );

			if ( $this.is( ".corner-all" ) ) {
				$this.removeClass( "corner-all" ).addClass( "corner-top" );
			} else if ( $this.is( ".corner-top" ) ) {
				$this.removeClass( "corner-top" ).addClass( "corner-all" );
			}
			event.preventDefault();
		});
	};

	// Color pickers setup (sets bg color of inputs)
	$.fn.applyFarbtastic = function() {
		return this.each(function() {
			$( "<div/>" ).farbtastic( this ).remove();
		});
	};

	/**
	 * App
	 */
	function appInit() {
		/* jqueryui.com site overrides for TR */
		$( "#content" ).attr( "id", "themeroller-content" );

		$( "#rollerTabs" ).tabs();

		themeGalleryInit();

		// General app click cleanup
		$( "body" ).on( "click", function( event ) {
			if ( $( event.target ).is( "input.hex.focus" ) || $( event.target ).parent().is( "div.texturePicker.focus" ) ) {
				return;
			}
			themeroller.find( "div.picker-on" ).removeClass( "picker-on" );
			$( "#picker" ).remove();
			themeroller.find( "input.focus, select.focus" ).removeClass( "focus" );
			themeroller.find( "div.texturePicker ul:visible" ).hide().parent().css( "position", "static" );
		});

		// Links to roll your own from help tab
		$( "#help a[href=\"#rollYourOwn\"]" ).on( "click", function( event ) {
			$( "#rollerTabs" ).tabs( "select", 0 );
			event.preventDefault();
		});

		// Links to theme gallery from help tab
		$( "#help a[href=\"#themeGallery\"]" ).on( "click", function( event ) {
			$( "#rollerTabs" ).tabs( "select", 1 );
			event.preventDefault();
		});

		$( "#reverse-background" ).on( "click", function() {
			var maskArea = themeroller.find( ".mask-area" ),
				textElems = themeroller.find( ".demoHeaders, #demo-options" );
			if ( $( this ).is( ":checked" ) ) {
				maskArea.css({ background: "#333" });
				textElems.css({ color: "#CCC" });
			} else {
				maskArea.css({ background: "#FFF" });
				textElems.css({ color: "#000" });
			}
		});
	}

	function rollYourOwnInit() {
		$( "#downloadTheme" ).on({
			"click": function() {
				var form = $( this ).parent().find( "form" );
				if ( form.find( ".state-error" ).length ) {
					// TODO: tell user submit has been cancelled, because there are errors!
					return false;
				}
			}
		});

		// Hover class toggles in app panel
		themeroller.find( "li.state-default, div.state-default" )
			.mouseenter(function() {
				$( this ).addClass( "state-hover" );
			})
			.mouseleave(function() {
				$( this ).removeClass( "state-hover" );
			});

		// Hex inputs
		themeroller.find( "input.hex" )
			.on({
				"change": function() {
					$( this ).trigger( "validate" );
				},
				"click": function( event ) {
					$( this ).addClass( "focus" );
					$( "#picker" ).remove();
					themeroller.find( "div.picker-on" ).removeClass( "picker-on" );
					themeroller.find( "div.texturePicker ul:visible" ).hide( 0 ).parent().css( "position", "static" );
					$( this ).after( "<div id=\"picker\"></div>" ).parent().addClass( "picker-on" );
					$( "#picker" ).farbtastic( this );
					event.preventDefault();
				},
				"validate": function() {
					// Validate hex colors
					if ( isHexColor( $( this ).val() ) ) {
						$( this ).removeClass( "state-error" );
					} else {
						$( this ).addClass( "state-error" );
					}
				}
			})
			.trigger( "validate" )
			.wrap( "<div class=\"hasPicker\"></div>" )
			.applyFarbtastic();

		// Focus and blur classes in form
		themeroller.find( "input, select" )
		.focus(function() {
			themeroller.find( "input.focus, select.focus" ).removeClass( "focus" );
			$( this ).addClass( "focus" );
		})
		.blur(function() {
			$( this ).removeClass( "focus" );
		});

		// Texture pickers from select menus
		themeroller.find( "select.texture" ).each(function() {

			$( this ).after( "<div class=\"texturePicker\"><a href=\"#\"></a><ul></ul></div>" );
			var texturePicker = $( this ).next(),
				a = texturePicker.find( "a" ),
				ul = texturePicker.find( "ul" ),
				sIndex = texturePicker.prev().get( 0 ).selectedIndex;

			// Scrape options
			$( this ).find( "option" ).each(function() {
				ul.append( "<li class=\"" + $( this ).attr( "value" ) + "\" data-texturewidth=\"" + $( this ).attr( "data-texturewidth" ) + "\" data-textureheight=\"" + $( this ).attr( "data-textureheight" ) + "\" style=\"background: #555555 url(" +  imageGeneratorUrl( $( this ).attr( "data-texturewidth" ), $( this ).attr( "data-textureheight" ), $( this ).attr( "value" ) ) + ") 50% 50% repeat\"><a href=\"#\" title=\"" + $( this ).text() + "\">" + $( this ).text() + "</a></li>" );
				if( $( this ).get( 0 ).index === sIndex ) {
					texturePicker.attr( "title", $( this ).text() ).css( "background", "#555555 url(" + imageGeneratorUrl( $( this ).attr( "data-texturewidth" ), $( this ).attr( "data-textureheight" ), $( this ).attr( "value" ) ) + ") 50% 50% repeat" );
				}
			});

			ul.find( "li" ).on( "click", function( event ) {
				texturePicker.prev().get( 0 ).selectedIndex = texturePicker.prev().find( "option[value="+ $( this ).attr( "class" ).replace( /\./g, "\\." ) +"]" ).get( 0 ).index;
				texturePicker.attr( "title", $( this ).text() ).css( "background", "#555555 url(" + imageGeneratorUrl( $( this ).attr( "data-texturewidth" ), $( this ).attr( "data-textureheight" ), $( this ).attr( "class" ) ) + ") 50% 50% repeat" );
				ul.fadeOut( 100 );
				formChange();
				event.preventDefault();
			});

			// Hide the menu and select el
			ul.hide();

			// Show/hide of menus
			texturePicker.on( "click", function( event ) {
				$( this ).addClass( "focus" );
				$( "#picker" ).remove();
				var showIt;
				if ( ul.is( ":hidden" ) ) {
					showIt = true;
				}
				themeroller.find( "div.texturePicker ul:visible" ).hide().parent().css( "position", "static" );
				if ( showIt === true ) {
					texturePicker.css( "position", "relative" );
					ul.show();
				}

				event.preventDefault();
			});
		});

		// Ensures numbers only are entered for opacity inputs
		themeroller.find( "input.opacity" ).on( "change", function() {
			var withinThreshold,
				number = parseInt( $( this ).val(), 10 );
			if( isNaN( number ) ) {
				$( this ).val( "" );
			} else {
				withinThreshold = Math.max( 0, Math.min( 100, number ) );
				if ( $( this ).val() !== withinThreshold ) {
					$( this ).val( withinThreshold );
				}
			}
		});

		// Spindowns in TR panel
		themeroller.find( "div.theme-group .theme-group-header" ).addClass( "corner-all" ).spinDown();

		// Change event in form
		themeroller.find( ".application form" ).on({
			"change": function( event ) {
				formChange();
				event.preventDefault();
			},
			"submit": function( event ) {
				event.preventDefault();
			},
			"validate": function() {
				var form = $( this ).parent().find( "form" );
				if ( form.find( ".state-error" ).length ) {
					$( "#downloadTheme" ).addClass( "ui-state-disabled" );
				} else {
					$( "#downloadTheme" ).removeClass( "ui-state-disabled" );
				}
			}
		});

		if ( openGroups.length > 0 ) {
			$.each( openGroups, function() {
				themeroller.find( ".theme-group-content:eq( " + this + " )" ).prev().trigger( "click" );
			});
		}
		if( focusedEl ) {
			themeroller.find( "form" ).find( "input, select, .texturePicker" ).eq( focusedEl ).click();
		}
	}

	function themeGalleryInit() {
		// Loading and viewing gallery themes
		$( "#themeGallery a" )
			.on( "click", function( event ) {
				model.set( QueryString.decode( this.href.split( "?" )[ 1 ] ) );
				event.preventDefault();
			})
			.attr( "title", "Click to preview this theme" )
			.each(function() {
				$( this ).after(
					"<a href=\"#\" class=\"download\" title=\"Download this theme\">Download</a>" +
					"<a href=\"#\" class=\"edit\" title=\"Customize this theme\">Edit</a>" );
			})
			.parent()
			.find( "a.edit" )
			.on( "click", function( event ) {
				reloadRollYourOwn = true;
				model.set( QueryString.decode( $( this ).parent().find( "a:first-child" ).attr( "href" ).split( "?" )[ 1 ] ) );
				$( "#rollerTabs" ).tabs( "select", 0 );
				event.preventDefault();
			});
		updateThemeGalleryDownloadLink();
	}

	function updateThemeGalleryDownloadLink() {
		$( "#themeGallery a.download" ).each(function() {
			var elem = $( this );
			model.downloadUrl(function( url ) {
				elem.attr( "href", url );
			}, elem.parent().find( "a:first-child" ).data( "z-theme-params" ) );
		});
	}

	function demoInit() {
		// Accordion
		$( "#accordion" ).accordion({ header: "h3" });

		// Autocomplete
		$( "#autocomplete" ).autocomplete({
			source: [ "c++", "java", "php", "coldfusion", "javascript", "asp", "ruby", "python", "c", "scala", "groovy", "haskell", "perl" ]
		});

		// Button
		$( "#button" ).button();
		$( "#radioset").buttonset();

		// Tabs
		$( "#tabs" ).tabs();

		// Dialog
		$( "#dialog" ).dialog({
			autoOpen: false,
			width: 600,
			buttons: [
				{
					text: "Ok",
					click: function() { $( this ).dialog( "close" ); }
				}, {
					text: "Cancel",
					click: function() { $( this ).dialog( "close" ); }
				}
			]
		});

		// Dialog Link
		$( "#dialog_link" ).on( "click", function( event ) {
			$( "#dialog" ).dialog( "open" );
			event.preventDefault();
		});

		// Datepicker
		$( "#datepicker" ).datepicker({
			inline: true
		});

		// Slider
		$( "#slider" ).slider({
			range: true,
			values: [ 17, 67 ]
		});

		// Progressbar
		$( "#progressbar" ).progressbar({
			value: 20
		});

		// Hover states on the static widgets
		$( "#dialog_link, #icons li" )
			.mouseenter(function() {
				$( this ).addClass( "ui-state-hover" );
			})
			.mouseleave(function() {
				$( this ).removeClass( "ui-state-hover" );
			});

		// Spinner
		$( "#spinner" ).spinner();

		// Menu
		$( "#menu" ).menu();

		// Tooltip
		themeroller.find( "p" ).tooltip({
			items: "img[alt]",
			content: function() {
				var alt = $( this ).attr( "alt" );
				// Escape alt, since we're going from an attribute to raw HTML
				return $( "<a>" ).text( alt ).html();
			}
		});
	}

	function rollYourOwnLoad() {
		var curr = ++lastRollYourOwnLoad,
			deferred = $.Deferred();

		// Roll Your Own:
		// Remember which groups are open
		openGroups = [];
		$( "div.theme-group-content" ).each(function( i ) {
			if ( $( this ).is( ":visible" ) ) {
				openGroups.push( i );
			}
		});

		// Remember any focused element
		focusedEl = null;
		themeroller.find( "form" ).find( "input, select, .texturePicker" ).each(function( i ) {
			if ( $( this ).is( ".focus" ) ) {
				focusedEl = i;
			}
		});

		rollYourOwnFetch().done(function( response ) {
			if ( curr !== lastRollYourOwnLoad ) {
				return;
			}
			$( "#rollYourOwn" ).html( response );
			rollYourOwnInit();
			deferred.resolve();
		}).fail(function() {
			if ( console && console.log ) {
				console.log( "Failed to reload rollYourOwn tab", arguments );
			}
		});
		
		return deferred;
	}

	model = new Model.ThemeRoller({
		baseVars: baseVars,
		host: downloadJqueryuiHost
	});

	model.on( "change", function ( changed ) {
		if ( "downloadParams" in changed ) {
			updateThemeGalleryDownloadLink();
		}
		if ( reloadRollYourOwn && !( "zThemeParams" in changed ) ) {
			reloadRollYourOwn = false;
			rollYourOwnLoad().done(function() {
				model.downloadUrl(function( url ) {
					$( "#downloadTheme" ).attr( "href", url );
				});
			});
		}
		model.downloadUrl(function( url ) {
			$( "#downloadTheme" ).attr( "href", url );
		});
		updateCSS();
		if ( skipHashChange ) {
			skipHashChange = false;
		} else {
			model.querystring().done(function( querystring ) {
				Hash.update( querystring, {
					ignoreChange: true
				});
			});
		}
	});

	Hash.on( "change", function( hash ) {
		reloadRollYourOwn = true;
		model.parseHash( hash );
	});

	skipHashChange = true;
	model.set( baseVars );

	appInit();
	rollYourOwnInit();
	demoInit();
	Hash.init();

}( jQuery, Hash, Model, QueryString ) );
