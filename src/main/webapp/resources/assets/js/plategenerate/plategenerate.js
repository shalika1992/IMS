/*
Merge Cells Function
@param - platesArray {}, mergeArray {}
 */
function _mergeCells(plateArray, mergeArray) {

    // arrange keys
    Object.keys(mergeArray).sort();

    // delete min key
    delete mergeArray[Object.keys(mergeArray)[0]];

    // delete other keys form array
    Object.keys(mergeArray).map(e => delete platesNum[e]);

    // new array for shift keys and values
    let newPlateArray = {};

    // undefined value count
    let undefineCount = 0;
    // shift the array
    for (let x = 0; x < (Object.keys(plateArray).length + Object.keys(mergeArray).length); x++) {
        // values shift (need to loop for length count)
        for (let d = 0; d < Object.keys(mergeArray).length; d++) {
            if (plateArray[x + undefineCount] === undefined) {
                undefineCount++;
            }
        }
        newPlateArray[x] = plateArray[x + undefineCount];
    }

    // remove undefined values form new array
    Object.keys(newPlateArray).map(e => {
        if (newPlateArray[e] === undefined) {
            delete newPlateArray[e];
        }
    });

    return newPlateArray;
}

/*
Generate Plates Function
@param - platesArray {}
 */
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
    let shift_val = 0;
    // monitor if pooled or not
    let pool_count = 0;
    // fill plates
    for (let k = 0; k < round; k++) {
        // plate rounds
        // check box
        let select = '<span style="float: right"><label class="checkbox checkbox-success text-dark"><input onchange="_checkBoxSelect(this.id,' + (k + 1) + ')" type="checkbox" name="selectAll" id="checkbox-' + (k + 1) + '"/><span style="margin-right: 5px;"></span>Select All</label></span>';

        if (platesArray[pool_count][0]['ispool'] === '0') {
            // plate no
            // not pooled
            html += "<div class='row master-plate' id='master-plate-" + (k + 1) + "'><div class='col-12 plate-title'>Master Plate<span>#" + (k + 1) + select + "</span></div>\n";
        } else {
            // plate no
            // pooled
            html += "<div class='row master-plate' id='master-plate-" + (k + 1) + "'><div class='col-12 plate-title'>Merged Plate<span>#" + (k + 1) + "</span></div>\n";
        }

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
        for (let j = 0; j < 8; j++) { // columns
            for (let i = 0; i < 12; i++) { // rows
                val = i + (12 * j); // change normal filling
                // [&& (val + 1) !== 27] => C3
                if ((val + 1) !== 84 && (val + 1) !== 96) {
                    if (platesArray[val + shift + shift_val] !== undefined) { // after finish filling
                        // tooltip creation
                        let ul = '<span class="label label-success label-inline ">Candidate Details</span>' +
                            '<ul style="text-align: left !important;" class="list-group">';
                        $.each(platesArray[val + shift + shift_val], (j, e) => {
                            for (let i = 0; i < e['id'].length; i++) {
                                ul += '<li style="text-align: left !important;" class="list-group-item">' + e['id'][i] + '</li>';
                                ul += '<li style="text-align: left !important;" class="list-group-item">' + e['referenceNo'][i] + '</li>';
                                ul += '<li style="text-align: left !important;" class="list-group-item">' + e['name'][i] + '</li>';
                                ul += '<li style="text-align: left !important;" class="list-group-item">' + e['nic'][i] + '</li>';
                            }
                            ul += '<li style="text-align: left !important;" class="list-group-item">' + e['labcode'] + '</li>';
                        });
                        ul += '</ul>';
                        // check if pooled or not
                        if (platesArray[pool_count][0]['ispool'] === '0') {
                            html += "<div data-html='true' data-toggle='tooltip' data-placement='right' class='col-1 cell-elmt cell-click plate-" + (k + 1) + "' data-cellNum='" + (val + 1) + "' data-key='" + (val + shift + shift_val) + "' data-value='" + platesArray[val + shift + shift_val][0]['labcode'] + "' title='" + ul + "'>" + platesArray[val + shift + shift_val][0]['labcode'] + "</div>\n";
                        } else {
                            html += "<div data-html='true' data-toggle='tooltip' data-placement='right' class='col-1 cell-elmt cell-disable plate-" + (k + 1) + "' data-cellNum='" + (val + 1) + "' data-key='" + (val + shift + shift_val) + "' data-value='" + platesArray[val + shift + shift_val][0]['labcode'] + "' title='" + ul + "'>" + platesArray[val + shift + shift_val][0]['labcode'] + "</div>\n";
                        }
                    } else {
                        html += "<div class='col-1 cell-elmt cell-disable' data-cellNum='" + (val + 1) + "' data-key='" + (val + shift + shift_val) + "'>N/A</div>\n";
                    }
                } else {
                    shift_val--;
                    // tooltip creation
                    if ((val + 1) === 84) {
                        let span = '<span class="label label-success label-inline ">Negative control</span>';
                        html += "<div class='col-1 cell-elmt cell-disable' data-html='true' data-toggle='tooltip' data-cellNum='" + (val + 1) + "' data-key='" + (val + shift + shift_val) + "'  title='" + span + "'>N/A</div>\n";
                    } else if ((val + 1) === 96) {
                        let span = '<span class="label label-success label-inline ">Positive control</span>';
                        html += "<div class='col-1 cell-elmt cell-disable' data-html='true' data-toggle='tooltip' data-cellNum='" + (val + 1) + "' data-key='" + (val + shift + shift_val) + "'  title='" + span + "'>N/A</div>\n";
                    } else {
                         html += "<div class='col-1 cell-elmt cell-disable' data-cellNum='" + (val + 1) + "' data-key='" + (val + shift + shift_val) + "'>N/A</div>\n";
                    }
                }
            }
        }

        // close tags
        html += "</div>\n";
        html += "</div>\n";
        html += '</div>\n'
        // to next plate
        shift += 96;
        pool_count += 94;

    }
    // set final html obj
    $('#plates').empty();
    $('#plates').append(html);

    // tooltip init
    $(function () {
        $('[data-toggle="tooltip"]').tooltip()
    })

}

/*
Swap Cells Function
@param - platesArray {}, cellArray {}
 */
function _swapCells(platesArray, cellArray) {

    // save previous values
    let _value1 = platesArray[Object.keys(cellArray)[0]];
    let _value2 = platesArray[Object.keys(cellArray)[1]];

    // swap values
    platesArray[Object.keys(cellArray)[0]] = _value2;
    platesArray[Object.keys(cellArray)[1]] = _value1;

    return platesArray;

}

// function to select all
function _checkBoxSelect(id, plateNo) {
    if ($("#" + id).is(':checked')) {
        console.log("checked");
        $('.cell-click.plate-' + plateNo).addClass('active');
    } else {
        console.log("unchecked");
        $('.cell-click.plate-' + plateNo).removeClass('active');
    }
}


// click events
$(document).on("click", ".cell-click", function () {
    if ($(this).hasClass('active')) {
        $(this).removeClass("active");
    } else {
        $(this).addClass("active");
    }
});
