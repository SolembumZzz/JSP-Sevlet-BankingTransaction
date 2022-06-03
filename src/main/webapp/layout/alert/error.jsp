<c:if test="${requestScope['error'] == 1}">
    <div class="fixed-bottom alert alert-danger alert-dismissible">
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        <strong>Error! </strong>${requestScope['message']}
    </div>
</c:if>