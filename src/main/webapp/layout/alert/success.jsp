<c:if test="${requestScope['error'] == 0}">
    <div class="fixed-bottom alert alert-success alert-dismissible">
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        <strong>Success! </strong>${requestScope["message"]}
    </div>
</c:if>