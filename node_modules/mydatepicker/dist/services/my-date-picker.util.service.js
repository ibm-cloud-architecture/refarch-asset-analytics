import { Injectable } from "@angular/core";
var M = "m";
var MM = "mm";
var MMM = "mmm";
var D = "d";
var DD = "dd";
var YYYY = "yyyy";
export var UtilService = (function () {
    function UtilService() {
        this.weekDays = ["su", "mo", "tu", "we", "th", "fr", "sa"];
    }
    UtilService.prototype.isDateValid = function (dateStr, dateFormat, minYear, maxYear, disableUntil, disableSince, disableWeekends, disableWeekDays, disableDays, disableDateRanges, monthLabels, enableDays) {
        var returnDate = { day: 0, month: 0, year: 0 };
        var daysInMonth = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
        var isMonthStr = dateFormat.indexOf(MMM) !== -1;
        var delimeters = this.getDateFormatDelimeters(dateFormat);
        var dateValue = this.getDateValue(dateStr, dateFormat, delimeters);
        var year = this.getNumberByValue(dateValue[0]);
        var month = isMonthStr ? this.getMonthNumberByMonthName(dateValue[1], monthLabels) : this.getNumberByValue(dateValue[1]);
        var day = this.getNumberByValue(dateValue[2]);
        if (month !== -1 && day !== -1 && year !== -1) {
            if (year < minYear || year > maxYear || month < 1 || month > 12) {
                return returnDate;
            }
            var date = { year: year, month: month, day: day };
            if (this.isDisabledDay(date, minYear, maxYear, disableUntil, disableSince, disableWeekends, disableWeekDays, disableDays, disableDateRanges, enableDays)) {
                return returnDate;
            }
            if (year % 400 === 0 || (year % 100 !== 0 && year % 4 === 0)) {
                daysInMonth[1] = 29;
            }
            if (day < 1 || day > daysInMonth[month - 1]) {
                return returnDate;
            }
            return date;
        }
        return returnDate;
    };
    UtilService.prototype.getDateValue = function (dateStr, dateFormat, delimeters) {
        var del = delimeters[0];
        if (delimeters[0] !== delimeters[1]) {
            del = delimeters[0] + delimeters[1];
        }
        var re = new RegExp("[" + del + "]");
        var ds = dateStr.split(re);
        var df = dateFormat.split(re);
        var da = [];
        for (var i = 0; i < df.length; i++) {
            if (df[i].indexOf(YYYY) !== -1) {
                da[0] = { value: ds[i], format: df[i] };
            }
            if (df[i].indexOf(M) !== -1) {
                da[1] = { value: ds[i], format: df[i] };
            }
            if (df[i].indexOf(D) !== -1) {
                da[2] = { value: ds[i], format: df[i] };
            }
        }
        return da;
    };
    UtilService.prototype.getMonthNumberByMonthName = function (df, monthLabels) {
        if (df.value) {
            for (var key = 1; key <= 12; key++) {
                if (df.value.toLowerCase() === monthLabels[key].toLowerCase()) {
                    return key;
                }
            }
        }
        return -1;
    };
    UtilService.prototype.getNumberByValue = function (df) {
        if (!/^\d+$/.test(df.value)) {
            return -1;
        }
        var nbr = Number(df.value);
        if (df.format.length === 1 && df.value.length !== 1 && nbr < 10 || df.format.length === 1 && df.value.length !== 2 && nbr >= 10) {
            nbr = -1;
        }
        else if (df.format.length === 2 && df.value.length > 2) {
            nbr = -1;
        }
        return nbr;
    };
    UtilService.prototype.getDateFormatDelimeters = function (dateFormat) {
        return dateFormat.match(/[^(dmy)]{1,}/g);
    };
    UtilService.prototype.parseDefaultMonth = function (monthString) {
        var month = { monthTxt: "", monthNbr: 0, year: 0 };
        if (monthString !== "") {
            var split = monthString.split(monthString.match(/[^0-9]/)[0]);
            month.monthNbr = split[0].length === 2 ? parseInt(split[0]) : parseInt(split[1]);
            month.year = split[0].length === 2 ? parseInt(split[1]) : parseInt(split[0]);
        }
        return month;
    };
    UtilService.prototype.formatDate = function (date, dateFormat, monthLabels) {
        var formatted = dateFormat.replace(YYYY, String(date.year));
        if (dateFormat.indexOf(MMM) !== -1) {
            formatted = formatted.replace(MMM, monthLabels[date.month]);
        }
        else if (dateFormat.indexOf(MM) !== -1) {
            formatted = formatted.replace(MM, this.preZero(date.month));
        }
        else {
            formatted = formatted.replace(M, String(date.month));
        }
        if (dateFormat.indexOf(DD) !== -1) {
            formatted = formatted.replace(DD, this.preZero(date.day));
        }
        else {
            formatted = formatted.replace(D, String(date.day));
        }
        return formatted;
    };
    UtilService.prototype.preZero = function (val) {
        return val < 10 ? "0" + val : String(val);
    };
    UtilService.prototype.isDisabledDay = function (date, minYear, maxYear, disableUntil, disableSince, disableWeekends, disableWeekDays, disableDays, disableDateRanges, enableDays) {
        for (var _i = 0, enableDays_1 = enableDays; _i < enableDays_1.length; _i++) {
            var e = enableDays_1[_i];
            if (e.year === date.year && e.month === date.month && e.day === date.day) {
                return false;
            }
        }
        var dn = this.getDayNumber(date);
        if (date.year < minYear && date.month === 12 || date.year > maxYear && date.month === 1) {
            return true;
        }
        var dateMs = this.getTimeInMilliseconds(date);
        if (this.isInitializedDate(disableUntil) && dateMs <= this.getTimeInMilliseconds(disableUntil)) {
            return true;
        }
        if (this.isInitializedDate(disableSince) && dateMs >= this.getTimeInMilliseconds(disableSince)) {
            return true;
        }
        if (disableWeekends) {
            if (dn === 0 || dn === 6) {
                return true;
            }
        }
        if (disableWeekDays.length > 0) {
            for (var _a = 0, disableWeekDays_1 = disableWeekDays; _a < disableWeekDays_1.length; _a++) {
                var wd = disableWeekDays_1[_a];
                if (dn === this.getWeekdayIndex(wd)) {
                    return true;
                }
            }
        }
        for (var _b = 0, disableDays_1 = disableDays; _b < disableDays_1.length; _b++) {
            var d = disableDays_1[_b];
            if (d.year === date.year && d.month === date.month && d.day === date.day) {
                return true;
            }
        }
        for (var _c = 0, disableDateRanges_1 = disableDateRanges; _c < disableDateRanges_1.length; _c++) {
            var d = disableDateRanges_1[_c];
            if (this.isInitializedDate(d.begin) && this.isInitializedDate(d.end) && dateMs >= this.getTimeInMilliseconds(d.begin) && dateMs <= this.getTimeInMilliseconds(d.end)) {
                return true;
            }
        }
        return false;
    };
    UtilService.prototype.isMarkedDate = function (date, markedDates, markWeekends) {
        for (var _i = 0, markedDates_1 = markedDates; _i < markedDates_1.length; _i++) {
            var md = markedDates_1[_i];
            for (var _a = 0, _b = md.dates; _a < _b.length; _a++) {
                var d = _b[_a];
                if (d.year === date.year && d.month === date.month && d.day === date.day) {
                    return { marked: true, color: md.color };
                }
            }
        }
        if (markWeekends && markWeekends.marked) {
            var dayNbr = this.getDayNumber(date);
            if (dayNbr === 0 || dayNbr === 6) {
                return { marked: true, color: markWeekends.color };
            }
        }
        return { marked: false, color: "" };
    };
    UtilService.prototype.isHighlightedDate = function (date, sunHighlight, satHighlight, highlightDates) {
        var dayNbr = this.getDayNumber(date);
        if (sunHighlight && dayNbr === 0 || satHighlight && dayNbr === 6) {
            return true;
        }
        for (var _i = 0, highlightDates_1 = highlightDates; _i < highlightDates_1.length; _i++) {
            var d = highlightDates_1[_i];
            if (d.year === date.year && d.month === date.month && d.day === date.day) {
                return true;
            }
        }
        return false;
    };
    UtilService.prototype.getWeekNumber = function (date) {
        var d = new Date(date.year, date.month - 1, date.day, 0, 0, 0, 0);
        d.setDate(d.getDate() + (d.getDay() === 0 ? -3 : 4 - d.getDay()));
        return Math.round(((d.getTime() - new Date(d.getFullYear(), 0, 4).getTime()) / 86400000) / 7) + 1;
    };
    UtilService.prototype.isMonthDisabledByDisableUntil = function (date, disableUntil) {
        return this.isInitializedDate(disableUntil) && this.getTimeInMilliseconds(date) <= this.getTimeInMilliseconds(disableUntil);
    };
    UtilService.prototype.isMonthDisabledByDisableSince = function (date, disableSince) {
        return this.isInitializedDate(disableSince) && this.getTimeInMilliseconds(date) >= this.getTimeInMilliseconds(disableSince);
    };
    UtilService.prototype.isInitializedDate = function (date) {
        return date.year !== 0 && date.month !== 0 && date.day !== 0;
    };
    UtilService.prototype.isSameDate = function (d1, d2) {
        return d1.year === d2.year && d1.month === d2.month && d1.day === d2.day;
    };
    UtilService.prototype.getTimeInMilliseconds = function (date) {
        return new Date(date.year, date.month - 1, date.day, 0, 0, 0, 0).getTime();
    };
    UtilService.prototype.getDayNumber = function (date) {
        return new Date(date.year, date.month - 1, date.day, 0, 0, 0, 0).getDay();
    };
    UtilService.prototype.getWeekDays = function () {
        return this.weekDays;
    };
    UtilService.prototype.getWeekdayIndex = function (wd) {
        return this.weekDays.indexOf(wd);
    };
    UtilService.decorators = [
        { type: Injectable },
    ];
    UtilService.ctorParameters = [];
    return UtilService;
}());
//# sourceMappingURL=my-date-picker.util.service.js.map