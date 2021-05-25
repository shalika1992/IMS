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

function _storeMergePlate(platesArray) {
    let finalArr = [];
    $(this).addClass("active");
    // get modulus
    let module = Object.keys(platesArray).length % 93;
    // get plate count
    let round = Math.floor(Object.keys(platesArray).length / 93);

    // plate count final
    if (module != 0) {
        round++;
    }
    let shift = 0;

    // plate rounds
    for (let k = 0; k < round; k++) {
        let val;
        let tmp = [];
        for (let j = 0; j < 12; j++) {
            for (let i = 0; i < 8; i++) {
                val = i + (8 * j);
                if (platesArray[val + shift] !== undefined) {
                    var remember = document.getElementById(k.toString());
                    if (remember.checked) {
                        tmp.push(platesArray[val + shift]);
                    }
                }
            }
        }
        shift += 96;
        if (finalArr.length === 0) {
            finalArr = tmp;
        } else {
            console.log("-------------------");
            let res = finalArr.map((value, index) => {
                return [value, tmp[index]]
            });
            console.log(res);
        }
    }
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
        for (let j = 0; j < 12; j++) { // columns
            for (let i = 0; i < 8; i++) { // rows
                val = i + (8 * j); // change normal filling
                if (platesArray[val + shift] !== undefined) { // after finish filling
                    // tooltip creation
                    let ul = '<span class="label label-success label-inline ">Candidate Details</span>' +
                        '<ul style="text-align: left !important;" class="list-group">';
                    $.each(platesArray[val + shift], (j, e) => {
                        ul += '<li style="text-align: left !important;" class="list-group-item">' + e + '</li>';
                    });
                    ul += '</ul>';
                    html += "<div data-html='true' data-toggle='tooltip' data-placement='right' class='col-1 cell-elmt cell-click' data-cellNum='" + (val + 1) + "' data-key='" + (val + shift) + "' data-value='" + platesArray[val + shift] + "' title='" + ul + "'>" + platesArray[val + shift][0] + "</div>\n";

                } else {
                    html += "<div class='col-1 cell-elmt cell-disable' data-cellNum='" + (val + 1) + "' data-key='" + (val + shift) + "'>N/A</div>\n";
                }
            }
        }

        // close tags
        html += "</div>\n";
        html += "</div>\n";
        html += '</div>\n'
        // to next plate
        shift += 96;

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

// click events
$(document).on("click", ".cell-click", function () {
    if ($(this).hasClass('active')) {
        $(this).removeClass("active");
    } else {
        $(this).addClass("active");
    }
});