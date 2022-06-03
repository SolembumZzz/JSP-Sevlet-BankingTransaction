<c:if test="${requestScope['error'] == 2}">
    <div class="fixed-bottom alert alert-warning alert-dismissible">
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        <strong>Error! </strong>${requestScope["message"]}
    </div>
</c:if>