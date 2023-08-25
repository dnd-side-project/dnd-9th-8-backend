package site.packit.packit.domain.email.dto;

public record SendEmailDto(
        String title,
        String content,
        String to,
        String from,
        String senderName
) {
    public static SendEmailDto of(
            String title,
            String content,
            String to,
            String from,
            String senderName
    ) {
        return new SendEmailDto(
                title,
                content,
                to,
                from,
                senderName
        );
    }
}