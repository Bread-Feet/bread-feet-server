package kr.co.breadfeetserver.infra.persistence.s3;

import java.util.List;

public interface AttachmentService<E, R> {

    void createAttachment(List<R> fileRequests, E entity);

    void deleteAttachment(E entity);

    void updateAttachment(List<R> fileRequests, E entity);
}
