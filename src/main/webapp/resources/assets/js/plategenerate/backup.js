function _generatePlates(platesArray) {
    // get modulus
    let module = Object.keys(platesArray).length % 93;
    // get plate count
    let round = Math.floor(Object.keys(platesArray).length / 93);

    // plate count final
    if (module != 0) {
        round++;
    }

    // html obj
    let html = "";
    // shift for next plate
    let shift = 0;
    // fill plates
    for (let k = 0; k < round; k++) { // plate rounds
        // plate no
        html += "<div class='row master-plate'><div class='col-12 plate-title'>Master Plate<span>#" + (k + 1) + "</span><span style='float:right;'>Select <input type='checkbox' style='margin-top: 5px;' id='" + k + "'></span></div>\n";

        // plate vertical letters
        html += "<div class='col-1'><div class='row'><div class='col-12 first-elmt'>&nbsp;</div></div>\n";
        for (let x = 1; x < 9; x++) {
            html += "<div class='row'><div class='col-12 vertical-elmt'>" + (x + 9).toString(36).toUpperCase() + "</div></div>\n";
        }
        html += "</div>\n";

        // plate horizontal numbers
        html += "<div class='col-11'>\n";
        html += "<div class='row'>\n";
        for (let y = 1; y < 13; y++) {
            html += "<div class='col-1 horizontal-elmt'>" + y + "</div>\n";
        }

        // fill cells
        let val;

        for (let i = 0; i < 8; i++) { // rows
            for (let j = 0; j < 12; j++) { // columns
                val = i + (8 * j); // change normal filling
                if (val < 94) { // ignore last 2 plates
                    if (platesArray[val + shift] !== undefined) { // after finish filling
                        // tooltip creation
                        let ul = '<span class="label label-success label-inline ">Candidate Details</span>' +
                            '<ul style="text-align: left !important;" class="list-group">';
                        $.each(platesArray[val + shift], (i, e) => {
                            ul += '<li style="text-align: left !important;" class="list-group-item">' + e + '</li>';
                        });
                        ul += '</ul>';
                        //

                        if ((val + 1) !== 19) {
                            html += "<div data-html='true' data-toggle='tooltip' data-placement='right' class='col-1 cell-elmt cell-click' data-cellNum='" + (val + 1) + "' data-key='" + (val + shift) + "' data-value='" + platesArray[val + shift] + "' title='" + ul + "'>" + platesArray[val + shift][0] + "</div>\n";
                        } else {
                            html += "<div class='col-1 cell-elmt cell-disable'>N/A</div>\n";
                        }

                    } else {
                        // html += "<div class='col-1 cell-elmt cell-disable' data-cellNum='" + (val + 1) + "' data-key='" + (val + shift) + "' data-value='" + platesArray[val + shift][0] + "'>N/A</div>\n";
                        html += "<div class='col-1 cell-elmt cell-disable' data-cellNum='" + (val + 1) + "' data-key='" + (val + shift) + "'>N/A</div>\n";
                    }
                } else {
                    // html += "<div class='col-1 cell-elmt cell-disable' data-cellNum='" + (val + 1) + "' data-key='" + (val + shift) + "' data-value='" + platesArray[val + shift][0] + "'>*</div>\n";
                    html += "<div class='col-1 cell-elmt cell-disable' data-cellNum='" + (val + 1) + "' data-key='" + (val + shift) + "'>*</div>\n";
                }
            }
        }
        // close tags
        html += "</div>\n";
        html += "</div>\n";
        html += '</div>\n'
        // to next plate
        shift += 94;

    }
    // set final html obj
    $('#plates').empty();
    $('#plates').append(html);

    // tooltip init
    $(function () {
        $('[data-toggle="tooltip"]').tooltip()
    })
}