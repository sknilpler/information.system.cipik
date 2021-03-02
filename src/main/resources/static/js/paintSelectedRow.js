function getParentTag(node, tag) {
    if (node) {
        return (node.tagName == tag) ? node : getParentTag(node.parentElement, tag);
    }
    return null;
}

var tb = document.getElementById('tb');
tb.setAttribute('activeRowIndex', 0);
tb.addEventListener('click', cli);
function cli(e) {
    var row = getParentTag(e.target, 'TR');
    if ((!row) || (row.rowIndex == 0)) {
        return;
    }
    var tbl = this, idx = tbl.getAttribute('activeRowIndex');
    tbl.rows[idx].classList.remove('activeRow');
    row.classList.add('activeRow');
    tbl.setAttribute('activeRowIndex', row.rowIndex);
}


